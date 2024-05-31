package com.azat4dev.booking.users.users_commands.domain.interfaces.repositories;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.entities.User;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import lombok.Getter;

import java.util.Optional;

public interface UsersRepository {

    void addNew(User newUserData) throws Exception.UserWithSameEmailAlreadyExists;

    void update(User user) throws Exception.UserNotFound;

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(EmailAddress email);

    // Exceptions

    abstract class Exception extends RuntimeException {

        protected Exception(String message) {
            super(message);
        }

        public String getCode() {
            return getClass().getSimpleName();
        }

        public static final class UserWithSameEmailAlreadyExists extends Exception {
            public UserWithSameEmailAlreadyExists() {
                super("User with same email already exists");
            }
        }

        @Getter
        public static final class UserNotFound extends Exception {

            private final UserId userId;

            public UserNotFound(UserId userId) {
                super("User not found");
                this.userId = userId;
            }
        }
    }
}