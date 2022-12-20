package at.fhtw.swen3.gps.service.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoEncodingCoordinate {
    private String lat;
    private String lon;
}
