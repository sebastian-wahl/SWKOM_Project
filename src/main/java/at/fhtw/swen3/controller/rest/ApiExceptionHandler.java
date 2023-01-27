package at.fhtw.swen3.controller.rest;

import at.fhtw.swen3.services.BLException;
import at.fhtw.swen3.services.dto.Error;
import at.fhtw.swen3.services.exception.blexception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            BLEntityValidationException.class,
            BLTrackingNumberExistException.class,
            BLInvalidHopArrivalCodeException.class,
            BLTrackingNumberExistException.class
    })
    public ResponseEntity<Error> handleEntityValidationException(BLException exception) {
        Error error = new Error().errorMessage(exception.getMessage());
        log.info(exception.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler({
            BLSubmitParcelAddressIncorrect.class,
            BLNoTruckFound.class,
            BLParcelNotFound.class,
            BLWarehouseNextHopsNotFound.class
    })
    public ResponseEntity<Error> handleRecipientAddressException(BLException exception) {
        Error error = new Error().errorMessage(exception.getMessage());
        log.info(exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Error> handleGenericException(Exception exception) {
        String message = "A general technical error occurred!";
        Error error = new Error().errorMessage(message);
        log.warn(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
