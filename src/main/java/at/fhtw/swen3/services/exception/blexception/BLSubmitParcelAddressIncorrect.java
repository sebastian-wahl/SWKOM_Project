package at.fhtw.swen3.services.exception.blexception;

import at.fhtw.swen3.gps.service.models.Address;
import at.fhtw.swen3.services.BLException;

public class BLSubmitParcelAddressIncorrect extends BLException {

    public static final String ERROR_MESSAGE = "Invalid %s address: %s";

    public BLSubmitParcelAddressIncorrect(Address address, boolean isSender) {
        super(String.format(ERROR_MESSAGE, isSender ? "sender" : "recipient", address));
    }
}
