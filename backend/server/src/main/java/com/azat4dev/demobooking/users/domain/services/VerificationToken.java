package com.azat4dev.demobooking.users.domain.services;

public final class VerificationToken {

    private final String value;

    public VerificationToken(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
