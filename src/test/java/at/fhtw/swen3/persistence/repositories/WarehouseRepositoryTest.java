package at.fhtw.swen3.persistence.repositories;


import at.fhtw.swen3.persistence.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WarehouseRepositoryTest {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    void GIVEN_saved_warehouseEntity_WHEN_findById_THEN_entity_found() {
        // GIVEN
        GeoCoordinateEntity geoCoordinateEntity = GeoCoordinateEntity.builder().build();

        WarehouseNextHopsEntity warehouseNextHopsEntity = WarehouseNextHopsEntity.builder()
                .traveltimeMins(1)
                .hop(buildHopEntity())
                .build();

        WarehouseEntity warehouseEntity = WarehouseEntity.builder()
                .code("ABCD1234")
                .hopType("warehouse")
                .description("description")
                .locationCoordinates(geoCoordinateEntity)
                .locationName("Vienna")
                .processingDelayMins(2)
                .level(1)
                .nextHop(warehouseNextHopsEntity)
                .build();

        // set warehouse for next hop
        warehouseNextHopsEntity.setWarehouse(warehouseEntity);


        warehouseRepository.save(warehouseEntity);

        // WHEN
        Optional<WarehouseEntity> foundWarehouse = warehouseRepository.findById(warehouseEntity.getId());

        // THEN
        assertThat(foundWarehouse).isPresent();
        assertThat(foundWarehouse.get())
                .usingRecursiveComparison()
                .isEqualTo(warehouseEntity);
    }

    private HopEntity buildHopEntity() {
        GeoCoordinateEntity geoCoordinateEntity = GeoCoordinateEntity.builder().build();

        return TruckEntity.builder()
                .code("ABCD1234")
                .hopType("warehouse")
                .description("description")
                .locationCoordinates(geoCoordinateEntity)
                .locationName("Vienna")
                .processingDelayMins(2)
                .regionGeoJson("regionGeoJson")
                .numberPlate("numberPlate")
                .build();
    }
}