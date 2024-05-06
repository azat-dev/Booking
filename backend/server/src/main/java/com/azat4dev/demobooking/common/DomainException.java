package com.azat4dev.demobooking.common;

public abstract class DomainException extends Exception {

    public DomainException(String message) {
        super(message);
    }

    public abstract String getCode();
}
