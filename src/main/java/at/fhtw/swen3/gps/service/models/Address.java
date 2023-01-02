package at.fhtw.swen3.gps.service.models;

import at.fhtw.swen3.persistence.entities.RecipientEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private String street;
    private String postalCode;
    private String city;
    private String country;

    public static Address fromRecipient(RecipientEntity recipient) {
        return Address.builder()
                .city(recipient.getCity())
                .country(recipient.getCountry())
                .postalCode(recipient.getPostalCode())//.replace("A", "AT"))
                .street(recipient.getStreet())
                .build();
    }

    public String toString() {
        return street + "; " + city + " " + postalCode + "; " + country;
    }

}
