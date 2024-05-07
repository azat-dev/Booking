package com.azat4dev.demobooking.users.data.repositories;

import com.azat4dev.demobooking.users.data.entities.UserData;
import com.azat4dev.demobooking.users.domain.entities.User;

@FunctionalInterface
public interface MapUserDataToDomain {

    User map(UserData userData);
}
