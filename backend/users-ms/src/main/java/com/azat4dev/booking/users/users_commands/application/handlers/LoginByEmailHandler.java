package com.azat4dev.booking.users.users_commands.application.handlers;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.users.users_commands.application.commands.LoginByEmail;
import com.azat4dev.booking.users.users_commands.domain.core.entities.User;

public interface LoginByEmailHandler {

    User handle(LoginByEmail command) throws Exception.WrongCredentials, ValidationException;

    // Exceptions

    abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public final static class WrongCredentials extends Exception {
            public WrongCredentials() {
                super("Wrong credentials");
            }
        }
    }
}
