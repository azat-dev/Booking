package com.azat4dev.demobooking.domain.identity.values;

public final class WrongPasswordFormatException extends Exception {
    public WrongPasswordFormatException() {
        super("Wrong format of password");
    }
}
