package at.fhtw.swen3.services.exception.BLException;


import java.util.List;

public class BLEntityValidationException extends BLException {

    private final List<String> validationMessages;

    public BLEntityValidationException(List<String> validationMessages) {
        super();
        this.validationMessages = validationMessages;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }
}
