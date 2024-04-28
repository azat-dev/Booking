package com.azat4dev.demobooking.domain.identity.values;

public final class Password {

    public final static int MIN_LENGTH = 6;
    public final static int MAX_LENGTH = 255;

    private final String value;

    private Password(String value) {
        this.value = value;
    }

    public static void validate(String password) throws WrongPasswordFormatException {
        if (
            password.length() < MIN_LENGTH ||
            password.length() > MAX_LENGTH ||
            password.matches(".*\\s+.*")
        ) {
            throw new WrongPasswordFormatException();
        }
    }

    public static Password makeFromString(String password) throws  WrongPasswordFormatException {
        validate(password);
        return new Password(password);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Password other)) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        return this.value.equals(other.getValue());
    }
}
