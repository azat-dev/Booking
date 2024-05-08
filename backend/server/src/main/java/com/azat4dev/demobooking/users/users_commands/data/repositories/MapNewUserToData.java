package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.NewUserData;

@FunctionalInterface
public interface MapNewUserToData {
    UserData map(NewUserData newUserData);
}
