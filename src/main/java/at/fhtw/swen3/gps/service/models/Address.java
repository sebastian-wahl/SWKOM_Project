package at.fhtw.swen3.gps.service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private String street;
    private String postalCode;
    private String city;
    private String country;
}
