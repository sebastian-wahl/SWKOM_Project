package at.fhtw.swen3.persistence.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Transferwarehouse
 */

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class TransferwarehouseEntity extends HopEntity {
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "REGION_GEO_JSON")
    private String regionGeoJson;

    @Column(name = "LOGISTICS_PARTNER")
    private String logisticsPartner;

    @Column(name = "LOGISTICS_PARTNER_URL")
    private String logisticsPartnerUrl;
}

