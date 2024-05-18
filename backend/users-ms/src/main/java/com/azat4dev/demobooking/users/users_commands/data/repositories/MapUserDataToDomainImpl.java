package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.LastName;
import com.azat4dev.demobooking.users.users_commands.domain.core.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.common.domain.values.UserId;

public class MapUserDataToDomainImpl implements MapUserDataToDomain {

    @Override
    public User map(UserData userData) throws DomainException {

        return User.dangerouslyMakeFrom(
            UserId.fromUUID(userData.id()),
            userData.createdAt(),
            userData.updatedAt(),
            EmailAddress.dangerMakeWithoutChecks(userData.email()),
            new FullName(
                FirstName.dangerMakeFromStringWithoutCheck(userData.firstName()),
                LastName.dangerMakeFromStringWithoutCheck(userData.lastName())
            ),
            new EncodedPassword(userData.encodedPassword()),
            userData.emailVerificationStatus(),
            userData.photo().map(UserData.PhotoPath::toDomain)
        );
    }
}
