package com.azat4dev.booking.users.users_commands.application.handlers;

import com.azat4dev.booking.common.presentation.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.application.commands.SignUp;

public interface SignUpHandler {

    UserId handle(SignUp command) throws ValidationException, Exception.UserWithSameEmailAlreadyExists;

    // Exception

    abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static class UserWithSameEmailAlreadyExists extends Exception {
            public UserWithSameEmailAlreadyExists() {
                super("User with same email already exists");
            }
        }
    }
}
