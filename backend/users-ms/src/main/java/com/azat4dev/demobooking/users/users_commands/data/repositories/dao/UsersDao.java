package com.azat4dev.demobooking.users.users_commands.data.repositories.dao;

import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;

import java.util.Optional;
import java.util.UUID;

public interface UsersDao {

    void addNew(UserData userData) throws UserAlreadyExistsException;

    Optional<UserData> findByEmail(String email);

    Optional<UserData> findById(UUID userId);

    public class UserAlreadyExistsException extends RuntimeException {
    }
}
