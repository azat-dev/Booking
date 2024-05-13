package com.azat4dev.demobooking.users.users_commands.domain.interfaces.services;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

public interface EmailVerificationTokensService {

    EmailVerificationToken generateFor(UserId userId, EmailAddress emailAddress);

    boolean verify(EmailVerificationToken token, UserId userId, EmailAddress emailAddress) throws TokenIsNotValidException, TokenExpiredException;

    // Exception

    static final class TokenIsNotValidException extends RuntimeException {
        public TokenIsNotValidException() {
            super("Token is not valid");
        }
    }

    static final class TokenExpiredException extends RuntimeException {
        public TokenExpiredException() {
            super("Token is expired");
        }
    }
}
