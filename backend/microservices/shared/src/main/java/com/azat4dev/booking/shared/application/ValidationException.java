package com.azat4dev.booking.shared.application;

import com.azat4dev.booking.shared.domain.DomainException;
import lombok.Getter;

import java.util.stream.Collectors;

@Getter
public final class ValidationException extends RuntimeException {

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

    @Override
    public String getMessage() {
        return error.errors().stream().map(Record::toString)
            .collect(Collectors.joining(", "));
    }
}
