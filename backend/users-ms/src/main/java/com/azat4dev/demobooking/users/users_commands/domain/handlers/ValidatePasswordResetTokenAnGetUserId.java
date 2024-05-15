package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.PasswordResetToken;

@FunctionalInterface
public interface ValidatePasswordResetTokenAnGetUserId {

    UserId execute(PasswordResetToken token) throws InvalidTokenException;

    // Exceptions

    final class InvalidTokenException extends RuntimeException {
    }

    final class TokenExpiredException extends RuntimeException {
    }
}
