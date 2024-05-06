package com.azat4dev.demobooking.common.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public final class ControllerException extends Exception {
    private final HttpStatus status;
    private final ErrorDTO body;

    public ControllerException(HttpStatus status, ErrorDTO body) {
        this.status = status;
        this.body = body;
    }

    public static ControllerException create(HttpStatus status, String errorCode, String errorMessage) {
        return new ControllerException(status, new ErrorDTO(errorCode, errorMessage));
    }

    public HttpStatus status() {
        return status;
    }

    public ErrorDTO body() {
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
