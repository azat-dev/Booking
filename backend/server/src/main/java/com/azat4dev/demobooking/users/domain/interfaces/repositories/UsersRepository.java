package com.azat4dev.demobooking.users.domain.interfaces.repositories;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.domain.entities.User;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;

import java.util.Optional;

public interface UsersRepository {

    void createUser(NewUserData newUserData) throws UserWithSameEmailAlreadyExistsException;

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(EmailAddress email);

    class UserWithSameEmailAlreadyExistsException extends DomainException {
        public UserWithSameEmailAlreadyExistsException() {
            super("User with same email already exists");
        }

        @Override
        public String getCode() {
            return "UserWithSameEmailAlreadyExists";
        }
    }
}