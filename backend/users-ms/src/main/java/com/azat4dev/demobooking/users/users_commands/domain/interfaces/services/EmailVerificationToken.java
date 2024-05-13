package com.azat4dev.demobooking.users.users_commands.domain.interfaces.services;

public record EmailVerificationToken(String value) {

    @Override
    public String toString() {
        return value;
    }
}
