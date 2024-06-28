package com.azat4dev.booking.users.commands.application.handlers;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.users.commands.application.commands.LoginByEmail;
import com.azat4dev.booking.users.commands.domain.core.entities.User;

public interface LoginByEmailHandler {

    User handle(LoginByEmail command) throws Exception.WrongCredentials, ValidationException;

    // Exceptions

    abstract class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static final class WrongCredentials extends Exception {
            public WrongCredentials() {
                super("Wrong credentials");
            }
        }
    }
}
