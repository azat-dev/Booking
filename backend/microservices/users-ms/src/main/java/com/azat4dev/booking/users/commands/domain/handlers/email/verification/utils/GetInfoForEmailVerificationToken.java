package com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils;

import com.azat4dev.booking.users.commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.booking.users.commands.domain.core.values.email.verification.EmailVerificationTokenInfo;

@FunctionalInterface
public interface GetInfoForEmailVerificationToken {

    EmailVerificationTokenInfo execute(EmailVerificationToken token) throws TokenIsNotValidException;

    // Exception

    final class TokenIsNotValidException extends RuntimeException {
        public TokenIsNotValidException() {
            super("Token is not valid");
        }
    }
}
