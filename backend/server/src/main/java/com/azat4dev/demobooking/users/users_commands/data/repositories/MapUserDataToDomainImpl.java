package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import com.azat4dev.demobooking.users.users_commands.domain.entities.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.entities.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.entities.LastName;
import com.azat4dev.demobooking.users.users_commands.domain.entities.User;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.values.UserId;

public class MapUserDataToDomainImpl implements MapUserDataToDomain {

    @Override
    public User map(UserData userData) throws DomainException {
        return new User(
            UserId.fromString(userData.getId().toString()),
            userData.getCreatedAt(),
            EmailAddress.makeFromString(userData.getEmail()),
            new FullName(
                new FirstName(userData.getFirstName()),
                new LastName(userData.getLastName())
            ),
            new EncodedPassword(userData.getEncodedPassword()),
            userData.getEmailVerificationStatus()
        );
    }
}
