package com.azat4dev.demobooking.domain.identity.values;

public final class Password {
    public final static int MIN_LENGTH = 6;

    private final String value;

    private Password(String value) {
        this.value = value;
    }

    public void validate(String password) throws WrongPasswordFormatException {

    }

    public static Password makeFromString(String password) {
        return new Password(password);
    }
}
