package at.fhtw.swen3.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Recipient
 */

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RecipientEntity implements BaseEntity {

    private String name;

    private String street;

    private String postalCode;

    private String city;

    private String country;
}

