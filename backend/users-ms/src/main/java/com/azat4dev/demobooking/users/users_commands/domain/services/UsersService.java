package com.azat4dev.demobooking.users.users_commands.domain.services;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.users_commands.domain.commands.CreateUser;

public interface UsersService {

    void handle(CreateUser command) throws UserWithSameEmailAlreadyExistsException;


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