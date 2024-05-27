package com.azat4dev.booking.shared.application;

public record ValidationErrorDetailsDTO(
    String code,
    String path,
    String error
) {

    public ValidationErrorDetailsDTO wrapInPath(String parentPath) {
        return new ValidationErrorDetailsDTO(code, parentPath + "." + path, error);
    }
}
