package at.fhtw.swen3.persistence.entity;

import at.fhtw.swen3.persistence.validation.annotation.NameCityValidation;
import at.fhtw.swen3.persistence.validation.annotation.PostalCodeValidation;
import at.fhtw.swen3.persistence.validation.annotation.StreetValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

/**
 * Recipient
 */

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RecipientEntity implements BaseEntity {

    @NameCityValidation
    private String name;

    @StreetValidation
    private String street;

    @PostalCodeValidation
    private String postalCode;

    @NameCityValidation
    private String city;

    private String country;
}

