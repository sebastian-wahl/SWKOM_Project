package at.fhtw.swen3.persistence.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Transferwarehouse
 */

@Getter
@Setter
public class TransferwarehouseEntity extends HopEntity implements BaseEntity {

    private String regionGeoJson;

    private String logisticsPartner;

    private String logisticsPartnerUrl;

    public TransferwarehouseEntity(String hopType, String code, String description, Integer processingDelayMins, String locationName, GeoCoordinateEntity locationCoordinates) {
        super(hopType, code, description, processingDelayMins, locationName, locationCoordinates);
    }
}

