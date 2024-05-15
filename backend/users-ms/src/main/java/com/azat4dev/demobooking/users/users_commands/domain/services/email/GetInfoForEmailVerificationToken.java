package com.azat4dev.demobooking.users.users_commands.domain.services.email;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.verification.EmailVerificationTokenInfo;

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
