package at.fhtw.swen3.util.impl;

import at.fhtw.swen3.util.HttpClient;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class ParcelLogisticsServiceClient implements HttpClient {

    private final String baseUrl;

    private final TestRestTemplate restTemplate;

    public ParcelLogisticsServiceClient(String baseUrl, TestRestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> ResponseEntity<T> get(String path, Class<T> responseClass) {
        HttpHeaders headers = buildHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(buildUrl(path), HttpMethod.GET, httpEntity, responseClass);
    }

    @Override
    public <T> ResponseEntity<T> post(String path, Object body, Class<T> responseClass) {
        HttpHeaders headers = buildHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(buildUrl(path), httpEntity, responseClass);
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private URI buildUrl(String path) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(path)
                .build()
                .toUri();
    }
}
