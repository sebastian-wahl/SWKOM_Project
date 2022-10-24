package at.fhtw.swen3.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Hop
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HopEntity {
    private String hopType;

    private String code;

    private String description;

    private Integer processingDelayMins;

    private String locationName;

    private GeoCoordinateEntity locationCoordinates;
}

