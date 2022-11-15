package at.fhtw.swen3.services.exception;


import java.util.List;

public class EntityValidationException extends RuntimeException {

    private final List<String> validationMessages;

    public EntityValidationException(List<String> validationMessages) {
        super();
        this.validationMessages = validationMessages;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }
}
