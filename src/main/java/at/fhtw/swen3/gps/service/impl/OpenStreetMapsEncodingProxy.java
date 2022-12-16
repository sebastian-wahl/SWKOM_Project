package at.fhtw.swen3.gps.service.impl;

import at.fhtw.swen3.gps.service.GeoEncodingService;
import at.fhtw.swen3.gps.service.models.Address;
import at.fhtw.swen3.gps.service.models.GeoEncodingCoordinate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

public class OpenStreetMapsEncodingProxy implements GeoEncodingService {

    public static final String OPENSTREETMAP_BASE_URL = "https://nominatim.openstreetmap.org/search";
    public static final String FORMAT = "jsonv2";
    public static final String FORMAT_PARAM = "format";
    public static final String COUNTRY_PARAM = "country";
    public static final String CITY_PARAM = "city";
    public static final String POSTALCODE_PARAM = "postalcode";
    public static final String STREET_PARAM = "street";

    private final RestTemplate restTemplate;

    public OpenStreetMapsEncodingProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<GeoEncodingCoordinate> encodeAddress(Address address) {
        GeoEncodingCoordinate[] coordinates = restTemplate.getForEntity(buildUrl(address), GeoEncodingCoordinate[].class)
                .getBody();
        return Optional.ofNullable(coordinates != null ? coordinates[0]: null);
    }

    String buildUrl(Address address) {
        return UriComponentsBuilder.fromHttpUrl(OPENSTREETMAP_BASE_URL)
                .queryParam(STREET_PARAM, address.getStreet())
                .queryParam(POSTALCODE_PARAM, address.getPostalCode())
                .queryParam(CITY_PARAM, address.getCity())
                .queryParam(COUNTRY_PARAM, address.getCountry())
                .queryParam(FORMAT_PARAM, FORMAT)
                .toUriString();
    }
}
