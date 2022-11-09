package at.fhtw.swen3.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

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
}

