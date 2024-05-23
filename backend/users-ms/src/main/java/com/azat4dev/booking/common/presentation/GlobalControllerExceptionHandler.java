package com.azat4dev.booking.common.presentation;

import com.azat4dev.booking.shared.domain.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<?> handleException(ValidationException ex) {
        return ex.toResponseEntity();
    }

    @ExceptionHandler({ControllerException.class})
    public ResponseEntity<?> handleException(ControllerException ex) {
        return ex.toResponseEntity();
    }

    @ExceptionHandler({DomainException.class})
    public ResponseEntity<?> handleException(DomainException ex) {
        return ControllerException.createError(HttpStatus.BAD_REQUEST, ex)
            .toResponseEntity();
    }
}
