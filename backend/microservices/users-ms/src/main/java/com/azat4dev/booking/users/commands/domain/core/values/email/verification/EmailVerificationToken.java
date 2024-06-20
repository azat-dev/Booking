package com.azat4dev.booking.users.commands.domain.core.values.email.verification;

public record EmailVerificationToken(String value) {

    @Override
    public String toString() {
        return value;
    }
}
