package com.azat4dev.demobooking.users.users_commands.domain.services;

public final class VerificationLink {

    private final String value;

    public VerificationLink(String value) {
        this.value = value;
    }

    public static VerificationLink make(String baseServerUrl, VerificationToken token) {
        return new VerificationLink(
            baseServerUrl + "?token=" + token.getValue()
        );
    }

    public String getValue() {
        return this.getValue();
    }
}
