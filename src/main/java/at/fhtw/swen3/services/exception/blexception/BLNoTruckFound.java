package at.fhtw.swen3.services.exception.blexception;

import at.fhtw.swen3.gps.service.models.GeoEncodingCoordinate;
import at.fhtw.swen3.services.BLException;

public class BLNoTruckFound extends BLException {

    public static final String ERROR_MESSAGE = "No Truck found for coordinates (%s, %s)!";

    public BLNoTruckFound(GeoEncodingCoordinate geoEncodingCoordinate) {
        super(String.format(ERROR_MESSAGE, geoEncodingCoordinate.getLat(), geoEncodingCoordinate.getLon()));
    }
}
