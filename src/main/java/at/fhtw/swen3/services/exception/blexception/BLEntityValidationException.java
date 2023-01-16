package at.fhtw.swen3.services.exception.blexception;

import at.fhtw.swen3.services.BLException;

import lombok.Getter;

import java.util.List;

public class BLEntityValidationException extends BLException {

    @Getter
    private final List<String> validationMessages;

    public BLEntityValidationException(List<String> validationMessages) {
        super(concatenateValidationMessages(validationMessages));
        this.validationMessages = validationMessages;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    private static String concatenateValidationMessages(List<String> validationMessages) {
        List<String> messages = validationMessages.stream().filter(message -> !message.isBlank()).toList();
        return String.join("; ", messages);
    }
}
