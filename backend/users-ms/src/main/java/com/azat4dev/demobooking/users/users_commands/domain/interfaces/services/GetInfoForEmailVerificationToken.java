package com.azat4dev.demobooking.users.users_commands.domain.interfaces.services;

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
