package at.fhtw.swen3.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * GeoCoordinate
 */

@Getter
@Setter
@AllArgsConstructor
public class GeoCoordinateEntity implements BaseEntity {
    private Double lat;

    private Double lon;
}

