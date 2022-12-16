package at.fhtw.swen3.gps.service.impl;

import at.fhtw.swen3.gps.service.models.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static at.fhtw.swen3.gps.service.impl.OpenStreetMapsEncodingProxy.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OpenStreetMapsEncodingProxyTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenStreetMapsEncodingProxy openStreetMapsEncodingProxy;

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