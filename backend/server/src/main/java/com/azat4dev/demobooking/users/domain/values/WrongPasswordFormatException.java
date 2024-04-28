package com.azat4dev.demobooking.users.domain.values;

public final class WrongPasswordFormatException extends Exception {
    public WrongPasswordFormatException() {
        super("Wrong format of password");
    }
}
