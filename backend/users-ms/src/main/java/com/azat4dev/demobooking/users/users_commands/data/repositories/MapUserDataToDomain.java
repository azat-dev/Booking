package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;

@FunctionalInterface
public interface MapUserDataToDomain {

    User map(UserData userData) throws DomainException;
}
