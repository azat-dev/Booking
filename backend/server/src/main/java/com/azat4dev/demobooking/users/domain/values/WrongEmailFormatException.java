package com.azat4dev.demobooking.users.domain.values;

public class WrongEmailFormatException extends Exception {

    private final String email;

    public WrongEmailFormatException(String email) {
        super("Wrong email format: " + email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
