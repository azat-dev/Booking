package com.azat4dev.booking.users.users_commands.domain.handlers.users;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.users.users_commands.domain.core.commands.CreateUser;

public interface UsersService {

    void handle(CreateUser command) throws Exception.UserWithSameEmailAlreadyExists;

    // Exceptions

    abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static final class UserWithSameEmailAlreadyExists extends DomainException {
            public UserWithSameEmailAlreadyExists() {
                super("User with same email already exists");
            }
        }
    }
}