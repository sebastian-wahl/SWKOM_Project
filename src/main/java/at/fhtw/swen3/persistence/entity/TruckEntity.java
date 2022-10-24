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
public class TruckEntity extends HopEntity {
    private String regionGeoJson;

    private String numberPlate;

    public TruckEntity(String hopType, String code, String description, Integer processingDelayMins, String locationName, GeoCoordinateEntity locationCoordinates, String regionGeoJson, String numberPlate) {
        super(hopType, code, description, processingDelayMins, locationName, locationCoordinates);
        this.regionGeoJson = regionGeoJson;
        this.numberPlate = numberPlate;
    }
}

