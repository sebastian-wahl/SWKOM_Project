package at.fhtw.swen3.gps.service.impl;

import at.fhtw.swen3.gps.service.models.Address;
import at.fhtw.swen3.gps.service.models.GeoEncodingCoordinate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static at.fhtw.swen3.gps.service.impl.OpenStreetMapsEncodingProxy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OpenStreetMapsEncodingProxyTest {

    public static final String LAT = "1.0";
    public static final String LON = "2.0";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenStreetMapsEncodingProxy openStreetMapsEncodingProxy;

    @Test
    void GIVEN_address_WHEN_encodeAddress_THEN_return_GPS_Coordinates() {
        doReturn(mockGeoEncodingCoordinates())
                .when(restTemplate)
                .getForEntity(anyString(), eq(GeoEncodingCoordinate[].class));

        Address address = Address.builder()
                .street("Höchstädtplatz")
                .postalCode("A-1200")
                .country("Austria")
                .city("Vienna")
                .build();

        Optional<GeoEncodingCoordinate> coordinateOpt = openStreetMapsEncodingProxy.encodeAddress(address);

        assertThat(coordinateOpt).isPresent();
        assertThat(coordinateOpt.get().getLat()).isEqualTo(LAT);
        assertThat(coordinateOpt.get().getLon()).isEqualTo(LON);
    }

    private ResponseEntity<GeoEncodingCoordinate[]> mockGeoEncodingCoordinates() {
        GeoEncodingCoordinate coordinate = GeoEncodingCoordinate.builder()
                .lat(LAT)
                .lon(LON)
                .build();
        return ResponseEntity.ok(new GeoEncodingCoordinate[]{coordinate});
    }

    @Test
    void GIVEN_address_WHEN_buildUrl_THEN_correct_url_with_query_params() {
        Address address = Address.builder()
                .street("street")
                .city("city")
                .postalCode("postalCode")
                .country("country")
                .build();

        String url = openStreetMapsEncodingProxy.buildUrl(address);

        assertThat(url)
                .contains(OPENSTREETMAP_BASE_URL)
                .contains(buildQueryParam(STREET_PARAM, address.getStreet()))
                .contains(buildQueryParam(CITY_PARAM, address.getCity()))
                .contains(buildQueryParam(POSTALCODE_PARAM, address.getPostalCode()))
                .contains(buildQueryParam(COUNTRY_PARAM, address.getCountry()))
                .contains(buildQueryParam(FORMAT_PARAM, FORMAT));
    }

    @Test
    void GIVEN_address_with_whitespaces_WHEN_buildUrl_THEN_correct_url_encoding() {
        Address address = Address.builder().street("street with whitespaces").build();
        String expectedStreet = "street%20with%20whitespaces";

        String url = openStreetMapsEncodingProxy.buildUrl(address);
        assertThat(url).contains(expectedStreet);
    }

    private String buildQueryParam(String paramName, String value) {
        return paramName + "=" + value;
    }
}