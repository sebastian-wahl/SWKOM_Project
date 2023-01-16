package at.fhtw.swen3.services.exception.blexception;

import at.fhtw.swen3.services.BLException;

public class BLParcelNotFound extends BLException {

    public static final String ERROR_MESSAGE = "Parcel with id %s was not found!";

    public BLParcelNotFound(String trackingId) {
        super(String.format(ERROR_MESSAGE, trackingId));
    }
}
