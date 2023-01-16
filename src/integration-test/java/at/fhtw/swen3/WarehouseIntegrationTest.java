package at.fhtw.swen3;

import at.fhtw.swen3.services.dto.Warehouse;
import at.fhtw.swen3.util.HttpClient;
import at.fhtw.swen3.util.impl.ParcelLogisticsServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.DoubleComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static at.fhtw.swen3.util.FileUtil.readFromFile;
import static org.assertj.core.api.Assertions.assertThat;

class WarehouseIntegrationTest extends BaseIntegrationTest {

    private HttpClient httpClient;

    private String warehouseBody;
    private Warehouse warehouse;

    @BeforeEach
    void setUp() throws IOException {
        String baseUrl = "http://localhost:" + port;
        httpClient = new ParcelLogisticsServiceClient(baseUrl, new TestRestTemplate());

        warehouseBody = readFromFile("trucks-light-transferwh.json");
        warehouse = new ObjectMapper().readValue(warehouseBody, Warehouse.class);
    }

    @Test
    void test_importWarehouses() {
        // 0. Import warehouses
        postImportWarehouses();

        // 1. Check exported warehouses
        getWarehouses();
    }

    private void postImportWarehouses() {
        // WHEN
        ResponseEntity<Void> response = httpClient.post("/warehouse", warehouseBody, Void.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void getWarehouses() {
        // WHEN
        ResponseEntity<Warehouse> response = httpClient.get("/warehouse", Warehouse.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .withComparatorForType(new DoubleComparator(0.000001), Double.class)
                .isEqualTo(warehouse);
    }
}
