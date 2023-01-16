package at.fhtw.swen3;

import at.fhtw.swen3.services.dto.HopArrival;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.TrackingInformation;
import at.fhtw.swen3.util.HttpClient;
import at.fhtw.swen3.util.impl.ParcelLogisticsServiceClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static at.fhtw.swen3.services.dto.TrackingInformation.StateEnum.*;
import static at.fhtw.swen3.util.FileUtil.readFromFile;
import static org.assertj.core.api.Assertions.assertThat;

class ParcelIntegrationTest extends BaseIntegrationTest {

    private HttpClient httpClient;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + port;
        httpClient = new ParcelLogisticsServiceClient(baseUrl, new TestRestTemplate());
        mapper = new ObjectMapper();
    }

    @Test
    void test_whole_parcel_tracking_flow() throws IOException {
        List<HopArrival> expectedVisitedHops = new ArrayList<>();
        List<HopArrival> expectedFutureHops = mapper.readValue(readFromFile("expected-hops.json"), new TypeReference<>(){});

        // 0.
        // Import all warehouses from json file
        postImportWarehouses();

        // 1.
        // Submit a parcel
        // Expect parcel tracking state to be PICKUP with correct visited and future hopArrivals
        String trackingId = postSubmitParcel();

        checkParcelTrackingInfoInState(trackingId, PICKUP, expectedVisitedHops, expectedFutureHops);

        // 2.
        // Report parcel hop at first Truck
        // Expect parcel tracking state to be INTRANSPORT with correct visited and future hopArrivals
        HopArrival nextFutureHop = moveFirstFutureHopToVisited(expectedVisitedHops, expectedFutureHops);
        postReportParcelHop(trackingId, nextFutureHop);

        checkParcelTrackingInfoInState(trackingId, INTRANSPORT, expectedVisitedHops, expectedFutureHops);

        // 3.
        // Report parcel hop at Warehouse
        // Expect parcel tracking state to be INTRANSPORT with correct visited and future hopArrivals
        HopArrival nextFutureHop2 = moveFirstFutureHopToVisited(expectedVisitedHops, expectedFutureHops);
        postReportParcelHop(trackingId, nextFutureHop2);

        checkParcelTrackingInfoInState(trackingId, INTRANSPORT, expectedVisitedHops, expectedFutureHops);

        // 4.
        // Report parcel hop at Truck
        // Expect parcel tracking state to be INTRUCKDELIVERY with correct visited and future hopArrivals
        HopArrival nextFutureHop3 = moveFirstFutureHopToVisited(expectedVisitedHops, expectedFutureHops);
        postReportParcelHop(trackingId, nextFutureHop3);

        checkParcelTrackingInfoInState(trackingId, INTRUCKDELIVERY, expectedVisitedHops, expectedFutureHops);

        // 5.
        // Report parcel delivery
        // Expect parcel tracking state to be DELIVERED with correct visited and future hopArrivals
        postReportParcelDelivery(trackingId);

        checkParcelTrackingInfoInState(trackingId, DELIVERED, expectedVisitedHops, expectedFutureHops);

    }

    private void postReportParcelDelivery(String trackingId) {
        // WHEN
        ResponseEntity<Void> response = httpClient.post("/parcel/" + trackingId + "/reportDelivery/", null, Void.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void postReportParcelHop(String trackingId, HopArrival nextHop) {
        // GIVEN
        String hopCode = nextHop.getCode();

        // WHEN
        ResponseEntity<Void> response = httpClient.post("/parcel/" + trackingId + "/reportHop/" + hopCode, null, Void.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void checkParcelTrackingInfoInState(
            String trackingId,
            TrackingInformation.StateEnum state,
            List<HopArrival> expectedVisitedHops,
            List<HopArrival> expectedFutureHops
    ) {
        // WHEN
        ResponseEntity<TrackingInformation> response = getTrackParcel(trackingId, state);

        // THEN
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody().getVisitedHops())
                .usingRecursiveFieldByFieldElementComparatorOnFields("code")
                .isEqualTo(expectedVisitedHops);

        assertThat(response.getBody().getFutureHops())
                .usingRecursiveFieldByFieldElementComparatorOnFields("code")
                .isEqualTo(expectedFutureHops);
    }

    private ResponseEntity<TrackingInformation> getTrackParcel(String trackingId, TrackingInformation.StateEnum expectedState) {
        // WHEN
        ResponseEntity<TrackingInformation> response = httpClient.get("/parcel/" + trackingId, TrackingInformation.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getState()).isEqualTo(expectedState);

        return response;
    }

    private void postImportWarehouses() throws IOException {
        // GIVEN
        String body = readFromFile("trucks-light-transferwh.json");

        // WHEN
        ResponseEntity<Void> response = httpClient.post("/warehouse", body, Void.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private String postSubmitParcel() throws IOException {
        // GIVEN
        String body = readFromFile("submit-parcel.json");

        // WHEN
        ResponseEntity<NewParcelInfo> response = httpClient.post("/parcel", body, NewParcelInfo.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody()).isNotNull();
        String trackingId = response.getBody().getTrackingId();
        assertThat(trackingId).isNotNull();

        return trackingId;
    }

    private HopArrival moveFirstFutureHopToVisited(List<HopArrival> expectedVisitedHops, List<HopArrival> expectedFutureHops) {
        HopArrival nextFutureHop = expectedFutureHops.remove(0);
        expectedVisitedHops.add(nextFutureHop);
        return nextFutureHop;
    }
}
