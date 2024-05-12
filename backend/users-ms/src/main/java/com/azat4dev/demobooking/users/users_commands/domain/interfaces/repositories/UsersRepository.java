package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.common.domain.values.UserId;

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