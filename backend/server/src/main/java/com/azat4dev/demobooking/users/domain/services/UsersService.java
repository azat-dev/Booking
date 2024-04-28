package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.users.domain.commands.CreateUser;
import com.azat4dev.demobooking.users.domain.values.WrongEmailFormatException;
import com.azat4dev.demobooking.users.domain.values.WrongPasswordFormatException;

public interface UsersService {

    void handle(CreateUser command) throws WrongEmailFormatException, WrongPasswordFormatException;
}