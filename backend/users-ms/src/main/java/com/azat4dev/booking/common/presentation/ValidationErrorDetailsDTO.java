package com.azat4dev.booking.common.presentation;

public record ValidationErrorDetailsDTO(
    String code,
    String path,
    String error
) {

    public ValidationErrorDetailsDTO wrapInPath(String parentPath) {
        return new ValidationErrorDetailsDTO(code, parentPath + "." + path, error);
    }
}
