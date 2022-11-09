package at.fhtw.swen3.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Transferwarehouse
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TransferwarehouseEntity extends HopEntity {
    @Column(name = "REGION_GEO_JSON")
    private String regionGeoJson;

    @Column(name = "LOGISTICS_PARTNER")
    private String logisticsPartner;

    @Column(name = "LOGISTICS_PARTNER_URL")
    private String logisticsPartnerUrl;

    @Builder
    public TransferwarehouseEntity(Long id, String hopType, String code, String description, Integer processingDelayMins, String locationName, @NotNull GeoCoordinateEntity locationCoordinates, String regionGeoJson, String logisticsPartner, String logisticsPartnerUrl) {
        super(id, hopType, code, description, processingDelayMins, locationName, locationCoordinates);
        this.regionGeoJson = regionGeoJson;
        this.logisticsPartner = logisticsPartner;
        this.logisticsPartnerUrl = logisticsPartnerUrl;
    }
}

