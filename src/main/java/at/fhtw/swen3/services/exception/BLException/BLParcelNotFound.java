package at.fhtw.swen3.services.exception.BLException;

import at.fhtw.swen3.services.BLException;

public class BLParcelNotFound extends BLException {

    public BLParcelNotFound(String trackingId) {
        this.message = "Parcel with id \"" + trackingId + "\" was not found!";
    }
}
