package com.azat4dev.booking.shared.application;

import com.azat4dev.booking.shared.domain.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public final class ControllerException extends RuntimeException {
    private final HttpStatus status;
    private final Object body;

    public ControllerException(HttpStatus status, Object body) {
        this.status = status;
        this.body = body;
    }

    public static ControllerException createError(HttpStatus status, String errorCode, String errorMessage) {
        return new ControllerException(status, new ErrorDTO(errorCode, errorMessage));
    }

    public static ControllerException createError(HttpStatus status, DomainException ex) {
        return new ControllerException(status, new ErrorDTO(ex.getCode(), ex.getMessage()));
    }

    public static ControllerException createValidationError(HttpStatus status, String path, String errorCode, String errorMessage) {
        return new ControllerException(status, ValidationErrorDTO.withError(errorCode, path, errorMessage));
    }

    public static ControllerException createValidationError(HttpStatus status, String path, DomainException exception) {
        return new ControllerException(status, ValidationErrorDTO.withError(exception.getCode(), path, exception.getMessage()));
    }

    public HttpStatus status() {
        return status;
    }

    public Object body() {
        return body;
    }

    public ResponseEntity<Object> toResponseEntity() {
        return ResponseEntity.status(status).body(body);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ControllerException) obj;
        return Objects.equals(this.status, that.status) &&
            Objects.equals(this.body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, body);
    }

    @Override
    public String toString() {
        return "ControllerException[" +
            "status=" + status + ", " +
            "body=" + body + ']';
    }
}
