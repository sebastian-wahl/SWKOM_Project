package at.fhtw.swen3.controller.rest;

import at.fhtw.swen3.services.dto.Error;
import at.fhtw.swen3.services.exception.EntityValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ EntityValidationException.class })
    public ResponseEntity<Error> handleEntityValidationException(EntityValidationException exception) {
        String message = String.join("; ", exception.getValidationMessages());
        Error error = new Error().errorMessage(message);
        log.info(message);

        return ResponseEntity.badRequest().body(error);
    }

}
