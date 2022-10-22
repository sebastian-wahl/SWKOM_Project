package at.fhtw.swen3.persistence.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Transferwarehouse
 */

@Getter
@Setter
public class Transferwarehouse extends Hop {

    private String regionGeoJson;

    private String logisticsPartner;

    private String logisticsPartnerUrl;

    public Transferwarehouse(String hopType, String code, String description, Integer processingDelayMins, String locationName, GeoCoordinate locationCoordinates) {
        super(hopType, code, description, processingDelayMins, locationName, locationCoordinates);
    }
}

