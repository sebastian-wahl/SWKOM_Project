package at.fhtw.swen3.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Hop
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HopEntity implements BaseEntity{
    private String hopType;

    private String code;

    // ToDo @WarehouseDescriptionValidation <- hier auch?
    private String description;

    private Integer processingDelayMins;

    private String locationName;

    @NotNull
    private GeoCoordinateEntity locationCoordinates;
}

