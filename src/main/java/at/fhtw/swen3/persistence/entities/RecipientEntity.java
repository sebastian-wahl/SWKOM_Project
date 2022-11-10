package at.fhtw.swen3.persistence.entities;

import at.fhtw.swen3.services.validation.annotation.NameCityValidation;
import at.fhtw.swen3.services.validation.annotation.PostalCodeValidation;
import at.fhtw.swen3.services.validation.annotation.StreetValidation;
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
@Table(name = "RECIPIENT")
public class RecipientEntity implements BaseEntity {
    @Column(name = "ID")
    @Id
    @GeneratedValue
    private Long id;

    @NameCityValidation
    @Column(name = "NAME")
    private String name;

    @StreetValidation
    @Column(name = "STREET")
    private String street;

    @PostalCodeValidation
    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @NameCityValidation
    @Column(name = "CITY")
    private String city;

    @Column(name = "COUNTRY")
    private String country;

    @JoinColumn(name = "PARCEL_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private ParcelEntity parcel;
}

