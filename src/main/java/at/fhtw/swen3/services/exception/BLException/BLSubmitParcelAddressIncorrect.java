package at.fhtw.swen3.services.exception.BLException;

import at.fhtw.swen3.gps.service.models.Address;
import at.fhtw.swen3.services.BLException;

public class BLSubmitParcelAddressIncorrect extends BLException {
    public BLSubmitParcelAddressIncorrect(Address recipientAddress, Address senderAddress) {
        if (recipientAddress != null) {
            this.message = "The recipient address is not valid: " + recipientAddress;
        }
        if (senderAddress != null) {
            this.message = "The sender address is not valid: " + senderAddress;
        }
    }
}
