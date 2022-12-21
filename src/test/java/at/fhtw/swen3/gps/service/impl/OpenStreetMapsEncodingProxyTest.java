package at.fhtw.swen3.gps.service.impl;

import at.fhtw.swen3.gps.service.models.Address;
import at.fhtw.swen3.gps.service.models.GeoEncodingCoordinate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
        doReturn(mockJsonResponse())
                .when(restTemplate)
                .getForObject(any(URI.class), eq(String.class));

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

    private String mockJsonResponse() {
        return String.format("""
            [
                {
                "lat": "%s",
                "lon": "%s",
                "unknownProperty": "test"
                }
            ]
            """, LAT, LON);
    }
}