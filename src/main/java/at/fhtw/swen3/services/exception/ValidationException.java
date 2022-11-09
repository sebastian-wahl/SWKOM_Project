package at.fhtw.swen3.services.exception;


import java.util.List;

public class ValidationException extends Exception {

    private final List<String> validationMessages;

    public ValidationException(List<String> validationMessages) {
        super();
        this.validationMessages = validationMessages;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }
}
