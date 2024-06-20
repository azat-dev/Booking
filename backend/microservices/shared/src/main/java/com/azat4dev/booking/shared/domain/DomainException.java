package com.azat4dev.booking.shared.domain;

public abstract class DomainException extends Exception {

    public DomainException(String message) {
        super(message);
    }

    public String getCode() {
        return getClass().getSimpleName();
    }
}
