package at.fhtw.swen3;

import at.fhtw.swen3.util.HttpClient;
import at.fhtw.swen3.util.impl.ParcelLogisticsServiceClient;
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

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + port;
        httpClient = new ParcelLogisticsServiceClient(baseUrl, new TestRestTemplate());
    }

    @Test
    void test_whole_parcel_tracking_flow() throws IOException {
        callImportWarehouses();
        callSubmitParcel();
    }

    private void callImportWarehouses() throws IOException {
        String body = readHopsFromFile("trucks-light-transferwh.json");

        ResponseEntity<String> response = httpClient.post("/warehouse", body, String.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void callSubmitParcel() throws IOException {
        String body = readHopsFromFile("submit-parcel.json");
        ResponseEntity<String> response = httpClient.post("/parcel", body, String.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("trackingId");
    }

    private String readHopsFromFile(String filename) throws IOException {
        return Files.readString(Paths.get("src/integration-test/resources/" + filename));
    }
}
