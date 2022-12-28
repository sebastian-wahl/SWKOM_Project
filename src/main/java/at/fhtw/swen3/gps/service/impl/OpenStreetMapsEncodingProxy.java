package at.fhtw.swen3.gps.service.impl;

import at.fhtw.swen3.gps.service.GeoEncodingService;
import at.fhtw.swen3.gps.service.models.Address;
import at.fhtw.swen3.gps.service.models.GeoEncodingCoordinate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
        URI url = buildUrl(address);
        log.debug("OpenStreetMaps url={}", url);
        try {
            String json = restTemplate.getForObject(url, String.class);
            log.debug("OpenStreetMaps response={}", json);

            return mapJsonToGeoCoordinateOptional(json);
        } catch (HttpClientErrorException e) {
            log.warn("Error while fetching geo coordinates");
            // TODO: rethrow exception
        }
        return Optional.empty();
    }

    private Optional<GeoEncodingCoordinate> mapJsonToGeoCoordinateOptional(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<GeoEncodingCoordinate> coordinates = objectMapper.readValue(json, new TypeReference<>(){});
            if (coordinates != null) {
                log.debug("coordinates={}", coordinates);
                return coordinates.stream().findFirst();
            }
        } catch (JsonProcessingException e) {
            log.warn("Parsing geoCoordinates form OpenStreetMaps failed", e);
        }
        return Optional.empty();
    }

    private URI buildUrl(Address address) {
        return UriComponentsBuilder.fromHttpUrl(OPENSTREETMAP_BASE_URL)
                .queryParam(STREET_PARAM, address.getStreet())
                .queryParam(POSTALCODE_PARAM, address.getPostalCode())
                .queryParam(CITY_PARAM, address.getCity())
                .queryParam(COUNTRY_PARAM, address.getCountry())
                .queryParam(FORMAT_PARAM, FORMAT)
                .build().toUri();
    }
}
