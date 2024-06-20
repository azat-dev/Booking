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

    sealed abstract class Exception extends DomainException permits Exception.InvalidToken, Exception.TokenExpired {
        Exception(String message) {
            super(message);
        }

        public static <Input extends ValidateTokenForPasswordResetAndGetUserId.Exception> Exception makeFrom(Input e) {
            switch (e) {
                case Input.TokenExpired inst -> {
                    return new TokenExpired();
                }
                case Input.InvalidToken inst -> {
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
