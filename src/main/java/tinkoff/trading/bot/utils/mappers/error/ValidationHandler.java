package tinkoff.trading.bot.utils.mappers.error;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class ValidationHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ValidationResponseObject> handleException(WebExchangeBindException e) {
        final List<ValidationError> errors = e.getBindingResult()
                                              .getAllErrors()
                                              .stream()
                                              .map(this::mapError)
                                              .collect(toList());
        return ResponseEntity.badRequest().body(new ValidationResponseObject(
                HttpStatus.BAD_REQUEST.value(),
                errors
        ));
    }

    private ValidationError mapError(ObjectError error) {
        if (error instanceof FieldError) {
            final var fieldError = (FieldError) error;
            return new ValidationError(
                    fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    fieldError.getRejectedValue()
            );
        } else {
            return new ValidationError(
                    null,
                    error.getDefaultMessage(),
                    null
            );
        }
    }

    @Value
    static class ValidationResponseObject {
        int                   statusCode;
        List<ValidationError> errors;
    }

    @Value
    static class ValidationError {
        String field;
        String errorMessage;
        Object rejectedValue;
    }

}
