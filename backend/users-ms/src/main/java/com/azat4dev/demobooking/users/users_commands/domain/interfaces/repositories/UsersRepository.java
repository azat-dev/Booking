package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

import java.util.Optional;

public interface UsersRepository {

    void addNew(User newUserData) throws UserWithSameEmailAlreadyExistsException;

    void update(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(EmailAddress email);

    // Exceptions

    static abstract class Exception extends RuntimeException {
        public Exception(String message) {
            super(message);
        }

        abstract String getCode();
    }

    static final class UserWithSameEmailAlreadyExistsException extends Exception {
        public UserWithSameEmailAlreadyExistsException() {
            super("User with same email already exists");
        }

        @Override
        public String getCode() {
            return "UserWithSameEmailAlreadyExists";
        }
    }

    static final class UserNotFoundException extends Exception {
        public UserNotFoundException() {
            super("User not found");
        }

        @Override
        public String getCode() {
            return "UserNotFound";
        }
    }
}