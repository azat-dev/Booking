package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.LastName;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.common.domain.values.UserId;

public class MapUserDataToDomainImpl implements MapUserDataToDomain {

    @Override
    public User map(UserData userData) throws DomainException {
        return new User(
            UserId.fromUUID(userData.id()),
            userData.createdAt(),
            EmailAddress.dangerMakeWithoutChecks(userData.email()),
            new FullName(
                FirstName.dangerMakeFromStringWithoutCheck(userData.firstName()),
                LastName.dangerMakeFromStringWithoutCheck(userData.lastName())
            ),
            new EncodedPassword(userData.encodedPassword()),
            userData.emailVerificationStatus()
        );
    }
}
