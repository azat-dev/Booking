package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;

@FunctionalInterface
public interface MapUserToData {
    UserData map(User newUserData);
}
