package com.azat4dev.demobooking.users.data.repositories;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.data.entities.UserData;
import com.azat4dev.demobooking.users.domain.entities.FirstName;
import com.azat4dev.demobooking.users.domain.entities.FullName;
import com.azat4dev.demobooking.users.domain.entities.LastName;
import com.azat4dev.demobooking.users.domain.entities.User;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;

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
