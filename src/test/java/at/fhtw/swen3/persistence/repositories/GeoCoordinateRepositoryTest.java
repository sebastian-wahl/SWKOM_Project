package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GeoCoordinateRepositoryTest {

    public static final double LAT = 1.0;
    public static final double LON = 2.0;
    @Autowired
    private GeoCoordinateRepository geoCoordinateRepository;

    @Test
    void GIVEN_saved_geoCoordinateEntity_WHEN_findById_THEN_entity_found() {
        // GIVEN
        GeoCoordinateEntity geoCoordinateEntity = GeoCoordinateEntity.builder()
                .lat(LAT)
                .lon(LON)
                .build();

        geoCoordinateRepository.save(geoCoordinateEntity);

        // WHEN
        Optional<GeoCoordinateEntity> foundEntity = geoCoordinateRepository.findById(geoCoordinateEntity.getId());

        // THEN
        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get())
                .usingRecursiveComparison()
                .isEqualTo(geoCoordinateEntity);

    }
}