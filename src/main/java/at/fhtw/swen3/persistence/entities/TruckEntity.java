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
 * Truck
 */

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class TruckEntity extends HopEntity {
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "REGION_GEO_JSON")
    private String regionGeoJson;

    @Column(name = "NUMBER_PLATE")
    private String numberPlate;
}

