package at.fhtw.swen3.services;

import lombok.Getter;

public class BLException extends RuntimeException {
    @Getter
    protected final String message;

    public BLException(String message) {
        super(message);
        this.message = message;
    }
}
