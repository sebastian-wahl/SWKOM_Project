package at.fhtw.swen3.services.exception.BLException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class BLException extends RuntimeException {
    @Getter
    protected String message;

}
