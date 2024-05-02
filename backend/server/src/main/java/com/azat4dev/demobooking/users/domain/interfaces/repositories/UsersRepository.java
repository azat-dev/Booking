package com.azat4dev.demobooking.users.domain.interfaces.repositories;

import com.azat4dev.demobooking.users.domain.entities.User;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;

import java.util.Optional;

public interface UsersRepository {

    void createUser(NewUserData newUserData);

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(EmailAddress email);
}