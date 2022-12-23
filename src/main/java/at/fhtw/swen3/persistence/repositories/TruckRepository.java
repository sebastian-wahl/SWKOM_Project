package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.TruckEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TruckRepository extends JpaRepository<TruckEntity, Long> {
    @Query(value =
        "SELECT * FROM truck_entity t " +
            "INNER JOIN hop h on t.id = h.id " +
            "INNER JOIN geo_coordinate gc on h.geo_coordinate_id " +
        "ORDER BY st_distance(gc.location, :location) " +
        "LIMIT 1",
    nativeQuery = true)
    Optional<TruckEntity> findFirstNearestTruck(@Param("location") Point location);
}