package at.fhtw.swen3.services.exception.BLException;

import lombok.Getter;

public class BLTrackingNumberExistException extends BLException {
    @Getter
    private final String message;

    public BLTrackingNumberExistException(String trackingNumber) {
        super();
        this.message = "Tracking number \"" + trackingNumber + "\" already exists!";
    }
}
