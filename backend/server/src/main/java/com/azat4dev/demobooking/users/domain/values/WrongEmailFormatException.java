package com.azat4dev.demobooking.users.domain.values;

import com.azat4dev.demobooking.common.DomainDataFormatException;

public class WrongEmailFormatException extends DomainDataFormatException {

    private final String email;

    public WrongEmailFormatException(String email) {
        super("Wrong email format: " + email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
