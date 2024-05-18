package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;

public final class MapUserToDataImpl implements MapUserToData {

    @Override
    public UserData map(User user) {
        return new UserData(
            user.getId().value(),
            user.getCreatedAt(),
            user.getCreatedAt(),
            user.getEmail().getValue(),
            user.getEncodedPassword().value(),
            user.getFullName().getFirstName().getValue(),
            user.getFullName().getLastName().getValue(),
            user.emailVerificationStatus(),
            user.getPhoto().map(UserData.PhotoPath::fromDomain)
        );
    }
}
