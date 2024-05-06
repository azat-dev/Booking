package com.azat4dev.demobooking.common.presentation;

import com.azat4dev.demobooking.common.DomainException;
import org.springframework.http.ResponseEntity;

public class ValidationException extends RuntimeException {

    private final ValidationErrorDTO error;

    private ValidationException(ValidationErrorDTO error) {
        super();
        this.error = error;
    }

    public static ValidationException withPath(String path, DomainException exception) {
        return new ValidationException(ValidationErrorDTO.withError(path, exception));
    }

    public static ValidationException with(String code, String path, String message) {
        return new ValidationException(ValidationErrorDTO.withError(code, path, message));
    }

    public ResponseEntity<ValidationErrorDTO> toResponseEntity() {
        return ResponseEntity.badRequest().body(error);
    }
}
