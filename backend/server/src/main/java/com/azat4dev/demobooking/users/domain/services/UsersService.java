package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.domain.commands.CreateUser;

public interface UsersService {

    class UserAlreadyExistsException extends DomainException {
        public UserAlreadyExistsException() {
            super("User already exists");
        }

        @Override
        public String getCode() {
            return "UserAlreadyExists";
        }
    }

    void handle(CreateUser command) throws UserAlreadyExistsException;
}