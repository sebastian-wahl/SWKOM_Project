package at.fhtw.swen3.persistence.entity;

import at.fhtw.swen3.persistence.validation.annotation.HopArrivalCodeValidation;
import at.fhtw.swen3.persistence.validation.annotation.WarehouseDescriptionValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * HopArrival
 */

@Getter
@Setter
@AllArgsConstructor
@Builder
public class HopArrivalEntity implements BaseEntity {
    @HopArrivalCodeValidation
    private String code;

    // ToDo @WarehouseDescriptionValidation <- hier auch?
    private String description;

    private OffsetDateTime dateTime;
}

