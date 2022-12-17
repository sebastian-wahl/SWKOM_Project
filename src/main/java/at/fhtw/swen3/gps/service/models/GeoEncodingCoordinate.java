package at.fhtw.swen3.gps.service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeoEncodingCoordinate {
    private String lat;
    private String lon;
}
