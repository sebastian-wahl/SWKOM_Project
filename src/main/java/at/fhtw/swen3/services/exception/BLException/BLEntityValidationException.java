package at.fhtw.swen3.services.exception.BLException;


import lombok.Getter;

import java.util.List;

public class BLEntityValidationException extends BLException {

    @Getter
    private final List<String> validationMessages;

    public BLEntityValidationException(List<String> validationMessages) {
        super();
        this.validationMessages = validationMessages;
    }

}
