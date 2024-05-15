package com.azat4dev.demobooking.users.users_commands.data.services.password;

import com.azat4dev.demobooking.users.common.domain.values.UserId;

import java.time.LocalDateTime;

@FunctionalInterface
public interface GetInfoForPasswordResetToken {

    Data execute(String token) throws InvalidTokenException;

    record Data(
        UserId userId,
        LocalDateTime expiresAt
    ) {
    }

    // Exceptions

    public static final class InvalidTokenException extends RuntimeException {
    }
}
