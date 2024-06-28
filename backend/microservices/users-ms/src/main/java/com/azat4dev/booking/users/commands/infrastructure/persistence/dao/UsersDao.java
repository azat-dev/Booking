package com.azat4dev.booking.users.commands.infrastructure.persistence.dao;

import com.azat4dev.booking.users.commands.infrastructure.entities.UserData;

import java.util.Optional;
import java.util.UUID;

public interface UsersDao {

    void addNew(UserData userData) throws Exception.UserAlreadyExists;

    void update(UserData userData) throws Exception.UserNotFound;

    Optional<UserData> findByEmail(String email);

    Optional<UserData> findById(UUID userId);

    // Exceptions

    abstract class Exception extends RuntimeException {

        public String getCode() {
            return getClass().getSimpleName();
        }

        public static final class UserAlreadyExists extends Exception {
        }

        public static final class UserNotFound extends Exception {
        }

        public static final class WrongJsonFormat extends Exception {
        }
    }
}
