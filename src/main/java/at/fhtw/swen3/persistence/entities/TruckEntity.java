package at.fhtw.swen3.persistence.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Truck
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TruckEntity extends HopEntity {
    @Column(name = "REGION_GEO_JSON")
    private String regionGeoJson;

    @Column(name = "NUMBER_PLATE")
    private String numberPlate;

    @Builder
    public TruckEntity(Long id, String hopType, String code, String description, Integer processingDelayMins, String locationName, @NotNull GeoCoordinateEntity locationCoordinates, String regionGeoJson, String numberPlate) {
        super(id, hopType, code, description, processingDelayMins, locationName, locationCoordinates);
        this.regionGeoJson = regionGeoJson;
        this.numberPlate = numberPlate;
    }
}

