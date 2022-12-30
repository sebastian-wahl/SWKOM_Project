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
        // 0. Import all warehouses from json file
        postImportWarehouses();

        // 1. Submit a parcel
        String trackingId = postSubmitParcel();

        // 2. Expect parcel tracking state to be PICKUP and futureHops have correct HopArrivals
        TrackingInformation trackingInformation = checkParcelTrackingInfoInStatePickup(trackingId);

        // 3. Report parcel hop
        postReportParcelHop(trackingId, findNextHop(trackingInformation.getFutureHops()));

        // 4. Expect parcel tracking state to be INTRANSPORT and futureHops have correct HopArrivals
        checkParcelTrackingInfoInStateInTransport(trackingId);

        // 5. Report parcel delivery
        postReportParcelDelivery(trackingId);

        // 6. Expect parcel tracking state to be DELIVERED and futureHops to be empty
        checkParcelTrackingInfoInStateInDelivered(trackingId);
    }

    private void postReportParcelDelivery(String trackingId) {
        // WHEN
        ResponseEntity<Void> response = httpClient.post("/parcel/" + trackingId + "/reportDelivery", null, Void.class);

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

    private TrackingInformation checkParcelTrackingInfoInStatePickup(String trackingId) throws IOException {
        // WHEN
        ResponseEntity<TrackingInformation> response = getTrackParcel(trackingId, PICKUP);

        // THEN
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody().getVisitedHops()).isEmpty();

        List<HopArrival> expectedFutureHops = mapper.readValue(readFromFile("expected-hops.json"), new TypeReference<>(){});
        assertThat(response.getBody().getFutureHops())
                .usingRecursiveFieldByFieldElementComparatorOnFields("code")
                .containsSequence(expectedFutureHops);

        return response.getBody();
    }

    private void checkParcelTrackingInfoInStateInTransport(String trackingId) throws IOException {
        // WHEN
        ResponseEntity<TrackingInformation> response = getTrackParcel(trackingId, INTRANSPORT);

        // THEN
        assertThat(response.getBody()).isNotNull();

        List<HopArrival> expectedHops = mapper.readValue(readFromFile("expected-hops.json"), new TypeReference<>(){});
        assertThat(response.getBody().getVisitedHops())
                .usingRecursiveFieldByFieldElementComparatorOnFields("code")
                .containsSequence(expectedHops.remove(0));

        assertThat(response.getBody().getFutureHops())
                .usingRecursiveFieldByFieldElementComparatorOnFields("code")
                .containsSequence(expectedHops);
    }


    private void checkParcelTrackingInfoInStateInDelivered(String trackingId) {
        // WHEN
        ResponseEntity<TrackingInformation> response = getTrackParcel(trackingId, DELIVERED);

        // THEN
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFutureHops()).isEmpty();
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

    private HopArrival findNextHop(List<HopArrival> futureHops) {
        assertThat(futureHops).hasSizeGreaterThanOrEqualTo(2);
        return futureHops.get(1);
    }
}
