package com.azat4dev.booking.users.users_commands.domain.handlers.users;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.commands.NewUserData;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;

public interface Users {

    void createNew(NewUserData newUserData) throws Exception.UserWithSameEmailAlreadyExists;

    void addVerifiedEmail(UserId userId, EmailAddress email) throws Exception.UserNotFound, Exception.EmailNotFound;

    // Exceptions

    abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static final class UserWithSameEmailAlreadyExists extends Exception {
            public UserWithSameEmailAlreadyExists() {
                super("User with same email already exists");
            }
        }

        public static final class UserNotFound extends Exception {
            public UserNotFound() {
                super("User not found");
            }
        }

        public static final class EmailNotFound extends Exception {
            public EmailNotFound() {
                super("Email not found");
            }
        }
    }
}