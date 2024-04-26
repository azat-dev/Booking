package com.azat4dev.demobooking.domain.identity.values;

public final class WrongPasswordFormatException extends RuntimeException {
    public WrongPasswordFormatException() {
        super("Wrong format of password");
    }
}
