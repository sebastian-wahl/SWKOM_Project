package at.fhtw.swen3.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Truck
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TruckEntity extends HopEntity {
    @Column(name = "REGION_GEO_JSON")
    private String regionGeoJson;

    @Column(name = "NUMBER_PLATE")
    private String numberPlate;
}

