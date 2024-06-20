package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.users.commands.infrastructure.entities.UserData;
import com.azat4dev.booking.users.commands.domain.core.entities.User;

@FunctionalInterface
public interface MapUserToData {
    UserData map(User newUserData);
}
