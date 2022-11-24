package at.fhtw.swen3.persistence.repositories;


import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static at.fhtw.swen3.util.JTSUtil.wktToGeometry;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GeoCoordinateRepositoryTest {

    @Autowired
    private GeoCoordinateRepository geoCoordinateRepository;

    @Test
    void GIVEN_saved_geoCoordinateEntity_WHEN_findById_THEN_entity_found() {
        // GIVEN
        GeoCoordinateEntity geoCoordinateEntity = GeoCoordinateEntity.builder()
                .location((Point) wktToGeometry("POINT(1 2)"))
                .build();

        geoCoordinateRepository.save(geoCoordinateEntity);

        // WHEN
        Optional<GeoCoordinateEntity> foundEntity = geoCoordinateRepository.findById(geoCoordinateEntity.getId());

        // THEN
        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get().getLocation().getX()).isEqualTo(1);
        assertThat(foundEntity.get().getLocation().getY()).isEqualTo(2);
    }
}