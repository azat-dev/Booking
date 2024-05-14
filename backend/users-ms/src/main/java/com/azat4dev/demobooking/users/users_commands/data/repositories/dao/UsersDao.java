package com.azat4dev.demobooking.users.users_commands.data.repositories.dao;

import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;

import java.util.Optional;
import java.util.UUID;

public interface UsersDao {

    void addNew(UserData userData) throws UserAlreadyExistsException;

    void update(UserData userData) throws  UserNotFound;

    Optional<UserData> findByEmail(String email);

    Optional<UserData> findById(UUID userId);

    // Exceptions

    public static class Exception extends RuntimeException {
    }

    public final static class UserAlreadyExistsException extends Exception {
    }

    public final static class UserNotFound extends Exception {
    }
}
