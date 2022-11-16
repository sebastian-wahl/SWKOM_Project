package at.fhtw.swen3.controller.rest;

import at.fhtw.swen3.services.dto.Error;
import at.fhtw.swen3.services.exception.EntityValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ EntityValidationException.class })
    public ResponseEntity<Error> handleEntityValidationException(EntityValidationException exception) {
        List<String> messages = exception.getValidationMessages().stream().filter(message -> !message.isBlank()).toList();
        String message = String.join("; ", messages);
        Error error = new Error().errorMessage(message);
        log.info(message);

        return ResponseEntity.badRequest().body(error);
    }

}
