package com.azat4dev.booking.common.presentation;

import com.azat4dev.booking.shared.domain.DomainException;

import java.util.List;

public record ValidationErrorDTO(List<ValidationErrorDetailsDTO> errors) {

    public final static String TYPE = "validationError";

    public static ValidationErrorDTO withError(
        String code,
        String path,
        String error
    ) {
        return new ValidationErrorDTO(List.of(new ValidationErrorDetailsDTO(code, path, error)));
    }

    public static ValidationErrorDTO withError(String path, DomainException exception) {
        return withError(exception.getCode(), path, exception.getMessage());
    }

    public String getType() {
        return TYPE;
    }
}
