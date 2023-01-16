package at.fhtw.swen3.util;

import org.springframework.http.ResponseEntity;

public interface HttpClient {
    <T> ResponseEntity<T> get(String path, Class<T> responseClass);
    <T> ResponseEntity<T> post(String path, Object body, Class<T> responseClass);
}
