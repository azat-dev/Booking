package com.azat4dev.demobooking.users.data.repositories;

import com.azat4dev.demobooking.users.data.entities.UserData;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.NewUserData;

public final class MapNewUserToDataImpl implements MapNewUserToData {

    @Override
    public UserData map(NewUserData newUserData) {
        return new UserData(
            newUserData.userId().value(),
            newUserData.createdAt(),
            newUserData.createdAt(),
            newUserData.email().toString(),
            newUserData.fullName().firstName().getValue(),
            newUserData.fullName().lastName().getValue(),
            newUserData.encodedPassword().value(),
            newUserData.emailVerificationStatus()
        );
    }
}
