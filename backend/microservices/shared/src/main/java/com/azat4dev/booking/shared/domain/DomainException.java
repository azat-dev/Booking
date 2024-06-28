package com.azat4dev.booking.shared.domain;

public abstract class DomainException extends Exception {

    protected DomainException(String message) {
        super(message);
    }

    public String getCode() {
        return getClass().getSimpleName();
    }
}
