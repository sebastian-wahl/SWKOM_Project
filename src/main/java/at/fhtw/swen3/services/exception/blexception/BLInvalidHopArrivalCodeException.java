package at.fhtw.swen3.services.exception.blexception;

import at.fhtw.swen3.services.BLException;

public class BLInvalidHopArrivalCodeException extends BLException {

    public static final String ERROR_MESSAGE = "Reporting arrival at hop with code %s not possible!";
    public BLInvalidHopArrivalCodeException(String hopCode) {
        super(String.format(ERROR_MESSAGE, hopCode));
    }
}