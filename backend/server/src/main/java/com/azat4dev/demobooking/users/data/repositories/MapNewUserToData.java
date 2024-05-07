package com.azat4dev.demobooking.users.data.repositories;

import com.azat4dev.demobooking.users.data.entities.UserData;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.NewUserData;

@FunctionalInterface
public interface MapNewUserToData {
    UserData map(NewUserData newUserData);
}
