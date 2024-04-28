package com.azat4dev.demobooking.domain.identity;

import com.azat4dev.demobooking.domain.identity.commands.CreateUser;

public interface UsersService {

    void handle(CreateUser command);
}