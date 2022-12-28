package at.fhtw.swen3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParcelIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void test_whole_parcel_tracking_flow() {

        callSubmitParcel();
    }

    private void callSubmitParcel() {
        String body = """
                {
                  "recipient": {
                    "name": "Recipient",
                    "street": "Höchstädtplatz 6",
                    "postalCode": "A-1200",
                    "city": "Wien",
                    "country": "Austria"
                  },
                  "sender": {
                    "name": "Sender",
                    "street": "Am Europlatz 3",
                    "postalCode": "A-1120",
                    "city": "Wien",
                    "country": "Austria"
                  },
                  "weight": 1.23
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(buildUrl("/parcel"), HttpMethod.POST, httpEntity, String.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private URI buildUrl(String path) {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + path)
                .build()
                .toUri();
    }
}
