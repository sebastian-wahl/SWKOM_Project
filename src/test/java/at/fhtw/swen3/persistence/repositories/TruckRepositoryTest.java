package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import at.fhtw.swen3.persistence.entities.TruckEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TruckRepositoryTest {

    @Autowired
    private TruckRepository truckRepository;

    @Test
    void GIVEN_saved_truckEntity_WHEN_findById_THEN_entity_found() {
        // GIVEN
        GeoCoordinateEntity geoCoordinateEntity = GeoCoordinateEntity.builder().build();

        TruckEntity truckEntity = TruckEntity.builder()
                .code("ABCD1234")
                .hopType("warehouse")
                .description("description")
                .locationCoordinates(geoCoordinateEntity)
                .locationName("Vienna")
                .processingDelayMins(2)
                .regionGeoJson("regionGeoJson")
                .numberPlate("numberPlate")
                .build();

        truckRepository.save(truckEntity);

        // WHEN
        Optional<TruckEntity> foundTruckEntity = truckRepository.findById(truckEntity.getId());

        // THEN
        assertThat(foundTruckEntity).isPresent();
        assertThat(foundTruckEntity.get())
                .usingRecursiveComparison()
                .isEqualTo(truckEntity);
    }
}