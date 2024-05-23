package com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils;

public record LinkForPasswordReset(String value) {

    public String toString() {
        return value;
    }
}
