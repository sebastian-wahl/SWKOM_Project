package at.fhtw.swen3.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Truck
 */

@Getter
@Setter
@AllArgsConstructor
public class Truck extends Hop {
    private String regionGeoJson;

    private String numberPlate;

    public Truck(String hopType, String code, String description, Integer processingDelayMins, String locationName, GeoCoordinate locationCoordinates, String regionGeoJson, String numberPlate) {
        super(hopType, code, description, processingDelayMins, locationName, locationCoordinates);
        this.regionGeoJson = regionGeoJson;
        this.numberPlate = numberPlate;
    }
}

