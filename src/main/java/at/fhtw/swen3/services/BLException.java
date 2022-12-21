package at.fhtw.swen3.services;

import lombok.Getter;

public class BLException extends RuntimeException {
    @Getter
    protected String message;
}
