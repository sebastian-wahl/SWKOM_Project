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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgisContainerProvider;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {ParcelIntegrationTest.Initializer.class})
@TestPropertySource("/integration-tests.properties")
@Testcontainers
class ParcelIntegrationTest {

    @Container
    public static JdbcDatabaseContainer postgis = new PostgisContainerProvider().newInstance("15-3.3-alpine");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgis.getJdbcUrl(),
                    "spring.datasource.username=" + postgis.getUsername(),
                    "spring.datasource.password=" + postgis.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @LocalServerPort
    private int port;

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
        // 1. Import all warehouses from json file
        postImportWarehouses();

        // 2. Submit a parcel
        String trackingId = postSubmitParcel();

        // 3. Expect parcel tracking state to be PICKUP and futureHops have correct HopArrivals
        getTrackParcel(trackingId);

    }

    private void getTrackParcel(String trackingId) throws IOException {
        // WHEN
        ResponseEntity<TrackingInformation> response = httpClient.get("/parcel/" + trackingId, TrackingInformation.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody().getState()).isEqualTo(TrackingInformation.StateEnum.PICKUP);
        assertThat(response.getBody().getVisitedHops()).isEmpty();

        List<HopArrival> expectedFutureHops = mapper.readValue(readFromFile("expected-future-hops.json"), new TypeReference<>(){});
        assertThat(response.getBody().getFutureHops()).usingRecursiveFieldByFieldElementComparatorOnFields("code")
                .containsSequence(expectedFutureHops);
    }

    private void postImportWarehouses() throws IOException {
        // GIVEN
        String body = readFromFile("trucks-light-transferwh.json");

        // WHEN
        ResponseEntity<String> response = httpClient.post("/warehouse", body, String.class);

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

    private String readFromFile(String filename) throws IOException {
        return Files.readString(Paths.get("src/integration-test/resources/" + filename));
    }
}
