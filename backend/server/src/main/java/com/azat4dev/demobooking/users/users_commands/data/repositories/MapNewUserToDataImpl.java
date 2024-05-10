package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.NewUserData;

public final class MapNewUserToDataImpl implements MapNewUserToData {

    @Override
    public UserData map(NewUserData newUserData) {
        return new UserData(
            newUserData.userId().value(),
            newUserData.createdAt(),
            newUserData.createdAt(),
            newUserData.email().toString(),
            newUserData.fullName().getFirstName().getValue(),
            newUserData.fullName().getLastName().getValue(),
            newUserData.encodedPassword().value(),
            newUserData.emailVerificationStatus()
        );
    }
}
