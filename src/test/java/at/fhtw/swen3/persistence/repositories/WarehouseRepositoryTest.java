package at.fhtw.swen3.persistence.repositories;


import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import at.fhtw.swen3.persistence.entities.WarehouseEntity;
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

        WarehouseEntity warehouseEntity = WarehouseEntity.builder()
                .code("ABCD1234")
                .hopType("warehouse")
                .description("description")
                .locationCoordinates(geoCoordinateEntity)
                .locationName("Vienna")
                .processingDelayMins(2)
                .level(1)
                .nextHops(Collections.emptyList())
                .build();

        geoCoordinateEntity.setHop(warehouseEntity);

        warehouseRepository.save(warehouseEntity);

        // WHEN
        Optional<WarehouseEntity> foundWarehouse = warehouseRepository.findById(warehouseEntity.getId());

        // THEN
        assertThat(foundWarehouse).isPresent();
        assertThat(foundWarehouse.get())
                .usingRecursiveComparison()
                .isEqualTo(warehouseEntity);
    }
}