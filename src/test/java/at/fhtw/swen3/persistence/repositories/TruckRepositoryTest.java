package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import at.fhtw.swen3.persistence.entities.TruckEntity;
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


    @Test
    void GIVEN_saved_truckEntity_WHEN_findById_THEN_entity_found() {
        // GIVEN
        TruckEntity truckEntity = buildTruck(10.0, 10.0, "Truck");
        truckRepository.save(truckEntity);

        // WHEN
        Optional<TruckEntity> foundTruckEntity = truckRepository.findById(truckEntity.getId());

        // THEN
        assertThat(foundTruckEntity).isPresent();
        assertThat(foundTruckEntity.get())
                .usingRecursiveComparison()
                .ignoringFields("id", "locationCoordinates")
                .isEqualTo(truckEntity);
    }

    @Test
    void GIVEN_truck_entities_WHEN_findNearestTruck_THEN_returns_correct_truck() {
        // GIVEN
        Point location = (Point) wktToGeometry(0.0, 0.0);

        TruckEntity firstTruck = buildTruck(1.0, 1.0, "Truck1");
        TruckEntity secondTruck = buildTruck(2.0, 2.0, "Truck2");
        TruckEntity thirdTruck = buildTruck(0.0, 0.5, "Truck3");

        truckRepository.saveAll(List.of(firstTruck, secondTruck, thirdTruck));

        // WHEN
        Optional<TruckEntity> foundTruckOpt = truckRepository.findFirstNearestTruck(location);

        // THEN
        assertThat(foundTruckOpt).isNotEmpty();
        assertThat(foundTruckOpt.get())
                .usingRecursiveComparison()
                .ignoringFields("id", "locationCoordinates")
                .isEqualTo(thirdTruck);
    }

    private TruckEntity buildTruck(double lat, double lon, String code) {
        GeoCoordinateEntity geoCoordinateEntity = GeoCoordinateEntity.builder()
                .location((Point) wktToGeometry(lat, lon))
                .build();

        return TruckEntity.builder()
                .code(code)
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