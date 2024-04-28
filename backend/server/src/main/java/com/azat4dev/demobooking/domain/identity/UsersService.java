package com.azat4dev.demobooking.domain.identity;

import com.azat4dev.demobooking.domain.identity.commands.CreateUser;
import com.azat4dev.demobooking.domain.identity.values.WrongEmailFormatException;
import com.azat4dev.demobooking.domain.identity.values.WrongPasswordFormatException;

public interface UsersService {

    void handle(CreateUser command) throws WrongEmailFormatException, WrongPasswordFormatException;
}