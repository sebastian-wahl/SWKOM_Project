package at.fhtw.swen3.services.exception.blexception;

import at.fhtw.swen3.services.BLException;

public class BLTrackingNumberExistException extends BLException {
    public BLTrackingNumberExistException(String trackingNumber) {
        super("Tracking number \"" + trackingNumber + "\" already exists!");
    }
}
