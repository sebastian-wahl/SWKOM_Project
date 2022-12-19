package at.fhtw.swen3.services.exception.BLException;

public class BLParcelNotFound extends BLException {

    public BLParcelNotFound(String trackingId) {
        super("Parcel with id \"" + trackingId + "\" was not found!");
    }
}
