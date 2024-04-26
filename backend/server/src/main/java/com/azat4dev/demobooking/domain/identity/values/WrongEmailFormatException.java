package com.azat4dev.demobooking.domain.identity.values;

public class WrongEmailFormatException extends RuntimeException {

    private final String email;

    public WrongEmailFormatException(String email) {
        super("Wrong email format: " + email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
