package com.azat4dev.demobooking.users.domain.interfaces.repositories;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.domain.entities.User;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;

import java.util.Optional;

public interface UsersRepository {

    class UserAlreadyExistsException extends DomainException {
        public UserAlreadyExistsException() {
            super("User already exists");
        }

        @Override
        public String getCode() {
            return "UserAlreadyExists";
        }
    }

    void createUser(NewUserData newUserData) throws UserAlreadyExistsException;

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(EmailAddress email);
}