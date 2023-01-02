package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import at.fhtw.swen3.persistence.entities.TruckEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static at.fhtw.swen3.util.JTSUtil.wktToGeometry;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/sql/h2gis_init.sql")
class TruckRepositoryTest {

    @Autowired
    private TruckRepository truckRepository;

    @BeforeEach
    void setUp() {
        truckRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        //truckRepository.deleteAll();
    }

    @Test
    void GIVEN_saved_truckEntity_WHEN_findById_THEN_entity_found() {
        // GIVEN
        TruckEntity truckEntity = buildTruck(0.0, 0.0);
        truckRepository.save(truckEntity);

        // WHEN
        Optional<TruckEntity> foundTruckEntity = truckRepository.findById(truckEntity.getId());

        // THEN
        assertThat(foundTruckEntity).isPresent();
        assertThat(foundTruckEntity.get())
                .usingRecursiveComparison()
                .ignoringFields("id", "locationCoordinates.id")
                .isEqualTo(truckEntity);
    }

    @Test
    void GIVEN_truck_entities_WHEN_findNearestTruck_THEN_returns_correct_truck() {
        // GIVEN
        Point location = (Point) wktToGeometry("POINT(0.0 0.0)");

        TruckEntity firstTruck = buildTruck(1.0, 1.0);
        TruckEntity secondTruck = buildTruck(2.0, 2.0);
        TruckEntity thirdTruck = buildTruck(0.0, 0.5);

        truckRepository.saveAll(List.of(firstTruck, secondTruck, thirdTruck));

        // WHEN
        Optional<TruckEntity> foundTruckOpt = truckRepository.findFirstNearestTruck(location);

        // THEN
        assertThat(foundTruckOpt).isNotEmpty();
        assertThat(foundTruckOpt.get())
                .usingRecursiveComparison()
                .ignoringFields("id", "locationCoordinates.id")
                .isEqualTo(thirdTruck);
    }

    private TruckEntity buildTruck(double lat, double lon) {
        GeoCoordinateEntity geoCoordinateEntity = GeoCoordinateEntity.builder()
                .location((Point) wktToGeometry(String.format("POINT(%f %f)", lat, lon)))
                .build();

        return TruckEntity.builder()
                .code("ABCD1234")
                .hopType("truck")
                .description("description")
                .locationCoordinates(geoCoordinateEntity)
                .locationName("Vienna")
                .processingDelayMins(2)
                .regionGeoJson("regionGeoJson")
                .numberPlate("numberPlate")
                .build();
    }
}