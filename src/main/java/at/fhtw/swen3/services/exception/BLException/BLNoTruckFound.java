package at.fhtw.swen3.services.exception.BLException;

import at.fhtw.swen3.gps.service.models.GeoEncodingCoordinate;
import at.fhtw.swen3.services.BLException;

public class BLNoTruckFound extends BLException {
    public BLNoTruckFound(GeoEncodingCoordinate geoEncodingCoordinate) {
        this.message = "No Truck found for coordinates \"" + geoEncodingCoordinate.getLat() + ", " + geoEncodingCoordinate.getLon() + "\"!";
    }
}
