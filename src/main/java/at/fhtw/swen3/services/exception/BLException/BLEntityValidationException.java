package at.fhtw.swen3.services.exception.BLException;


import java.util.List;

public class BLEntityValidationException extends BLException {

    private final List<String> validationMessages;

    public BLEntityValidationException(List<String> validationMessages) {
        this.validationMessages = validationMessages;
        this.message = concatenateValidationMessages();
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    private String concatenateValidationMessages() {
        List<String> messages = validationMessages.stream().filter(message -> !message.isBlank()).toList();
        return String.join("; ", messages);
    }
}
