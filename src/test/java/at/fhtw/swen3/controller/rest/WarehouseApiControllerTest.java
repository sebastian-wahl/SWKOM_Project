package at.fhtw.swen3.controller.rest;

import at.fhtw.swen3.controller.WarehouseApi;
import at.fhtw.swen3.persistence.entities.*;
import at.fhtw.swen3.services.WarehouseService;
import at.fhtw.swen3.services.dto.GeoCoordinate;
import at.fhtw.swen3.services.dto.Hop;
import at.fhtw.swen3.services.dto.Warehouse;
import at.fhtw.swen3.services.dto.WarehouseNextHops;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ValidationException;
import java.util.Optional;

import static at.fhtw.swen3.util.JTSUtil.wktToGeometry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;

@SpringBootTest
class WarehouseApiControllerTest {

    public static final String VALID_CODE = "ABCD123";
    @MockBean
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseApi warehouseApiController;

    @Test
    void GIVEN_export_successful_WHEN_exportWarehouses_THEN_200_ok() {
        doReturn(mockWarehouseEntity()).when(warehouseService).exportWarehouses();

        ResponseEntity<Warehouse> response = warehouseApiController.exportWarehouses();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    // ToDo remove disabled
    @Test
    @Disabled
    void GIVEN_warehouse_not_loaded_WHEN_exportWarehouses_THEN_404_not_found() {
        doReturn(null).when(warehouseService).exportWarehouses();
        ResponseEntity<Warehouse> response = warehouseApiController.exportWarehouses();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void GIVEN_code_WHEN_getWarehouse_THEN_200_ok() {
        doReturn(Optional.of(mockTruckEntity())).when(warehouseService).getWarehouse(VALID_CODE);

        ResponseEntity<Hop> response = warehouseApiController.getWarehouse(VALID_CODE);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    // ToDo remove disabled
    @Test
    @Disabled
    void GIVEN_not_found_for_code_WHEN_getWarehouse_THEN_404_not_found() {
        doReturn(Optional.empty()).when(warehouseService).getWarehouse(anyString());

        ResponseEntity<Hop> response = warehouseApiController.getWarehouse(VALID_CODE);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void GIVEN_valid_warehouse_dto_WHEN_importWarehouses_THEN_200_ok() {
        Warehouse warehouseDto = mockWarehouse();

        ResponseEntity<Void> response = warehouseApiController.importWarehouses(warehouseDto);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void GIVEN_invalid_warehouse_dto_WHEN_importWarehouses_THEN_ValidationException() {
        Warehouse warehouseDto = mockWarehouse();
        warehouseDto.setNextHops(null);

        assertThatCode(() -> warehouseApiController.importWarehouses(warehouseDto))
                .isInstanceOf(ValidationException.class);

    }

    private Warehouse mockWarehouse() {
        WarehouseNextHops warehouseNextHops = new WarehouseNextHops()
                .hop(mockHop())
                .traveltimeMins(10);

        return new Warehouse()
                .level(3)
                .code(VALID_CODE)
                .description("description")
                .hopType("hopType")
                .locationName("locationName")
                .processingDelayMins(1)
                .locationCoordinates(new GeoCoordinate().lat(0.0).lon(2.0))
                .addNextHopsItem(warehouseNextHops);
    }

    private Optional<WarehouseEntity> mockWarehouseEntity() {
        WarehouseNextHopsEntity warehouseNextHops = WarehouseNextHopsEntity.builder()
                .hop(mockHopEntity())
                .traveltimeMins(10)
                .build();

        return Optional.of(WarehouseEntity.builder()
                .level(3)
                .code(VALID_CODE)
                .description("description")
                .hopType("hopType")
                .locationName("locationName")
                .processingDelayMins(1)
                .locationCoordinates(GeoCoordinateEntity.builder()
                        .lat(1.0)
                        .lon(2.0)
                        .build())
                .nextHop(warehouseNextHops)
                .build());
    }

    private HopEntity mockHopEntity() {
        return HopEntity.builder()
                .code(VALID_CODE)
                .description("description")
                .hopType("hopType")
                .locationName("locationName")
                .processingDelayMins(1)
                .locationCoordinates(GeoCoordinateEntity.builder()
                        .lat(1.0)
                        .lon(2.0)
                        .build())
                .build();
    }

    private HopEntity mockTruckEntity() {
        return TruckEntity.builder()
                .code(VALID_CODE)
                .description("description")
                .hopType("hopType")
                .locationName("locationName")
                .processingDelayMins(1)
                .locationCoordinates(GeoCoordinateEntity.builder()
                        .lat(1.0)
                        .lon(2.0)
                        .build())
                .build();
    }

    private Hop mockHop() {
        return new Hop()
                .code(VALID_CODE)
                .description("description")
                .hopType("hopType")
                .locationName("locationName")
                .processingDelayMins(1)
                .locationCoordinates(new GeoCoordinate().lat(1.0).lon(2.0));
    }

}