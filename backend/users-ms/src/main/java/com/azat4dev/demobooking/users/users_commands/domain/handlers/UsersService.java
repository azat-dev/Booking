package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CreateUser;

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