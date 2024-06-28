package com.azat4dev.booking.users.commands.domain.handlers.password.reset;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.users.commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils.ValidateTokenForPasswordResetAndGetUserId;


public interface SetNewPasswordByToken {

    void execute(
        IdempotentOperationId operationId,
        TokenForPasswordReset token,
        EncodedPassword encodedPassword
    ) throws Exception.InvalidToken, Exception.TokenExpired;

    // Exceptions

    abstract sealed class Exception extends DomainException {
        Exception(String message) {
            super(message);
        }

        public static <I extends ValidateTokenForPasswordResetAndGetUserId.Exception> Exception makeFrom(I e) {
            switch (e) {
                case I.TokenExpired inst -> {
                    return new TokenExpired();
                }
                case I.InvalidToken inst -> {
                    return new InvalidToken();
                }
            }
        }

        public static final class InvalidToken extends Exception {
            public InvalidToken() {
                super("Invalid token");
            }
        }

        public static final class TokenExpired extends Exception {
            public TokenExpired() {
                super("Token is expired");
            }
        }
    }
}
