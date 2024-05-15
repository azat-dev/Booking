package com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.utils;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;

@FunctionalInterface
public interface ValidateTokenForPasswordResetAndGetUserId {

    UserId execute(TokenForPasswordReset token) throws InvalidTokenException, TokenExpiredException;

    // Exceptions

    final class InvalidTokenException extends RuntimeException {
    }

    final class TokenExpiredException extends RuntimeException {
    }
}
