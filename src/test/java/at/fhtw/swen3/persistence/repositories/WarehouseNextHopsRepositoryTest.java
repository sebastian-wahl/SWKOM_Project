package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.TruckEntity;
import at.fhtw.swen3.persistence.entities.WarehouseNextHopsEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WarehouseNextHopsRepositoryTest {
    @Autowired
    private WarehouseNextHopsRepository warehouseNextHopsRepository;

    @Test
    void GIVEN_saved_warehouseEntity_WHEN_findById_THEN_entity_found() {
        // GIVEN
        HopEntity hopEntity = buildHopEntity();

        WarehouseNextHopsEntity warehouseNextHopsEntity = WarehouseNextHopsEntity.builder()
                .traveltimeMins(1)
                .hop(hopEntity)
                .build();
        hopEntity.setReferencedNextHop(warehouseNextHopsEntity);

        warehouseNextHopsRepository.save(warehouseNextHopsEntity);

        // WHEN
        Optional<WarehouseNextHopsEntity> foundEntity = warehouseNextHopsRepository.findById(warehouseNextHopsEntity.getId());

        // THEN
        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get())
                .usingRecursiveComparison()
                .ignoringFields("hop.locationCoordinates")
                .isEqualTo(warehouseNextHopsEntity);
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