package at.fhtw.swen3.persistence.entities;

import at.fhtw.swen3.services.validation.annotation.*;
import lombok.*;

import javax.persistence.*;

/**
 * Recipient
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ConditionalValidations(
        field = "country", contains = {"Austria", "Österreich"}
)
@Table(name = "RECIPIENT")
public class RecipientEntity implements BaseEntity {
    @Column(name = "ID")
    @Id
    @GeneratedValue
    private Long id;

    @NameCityValidation
    @Column(name = "NAME")
    private String name;

    @StreetValidation(groups = {ValidateUnderCondition.class})
    @Column(name = "STREET")
    private String street;

    @PostalCodeValidation(groups = {ValidateUnderCondition.class})
    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @NameCityValidation(groups = {ValidateUnderCondition.class})
    @Column(name = "CITY")
    private String city;

    @Column(name = "COUNTRY")
    private String country;
}

