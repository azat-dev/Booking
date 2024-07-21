package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.infrastructure.entities.UserData;
import com.azat4dev.booking.users.commands.domain.core.entities.User;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.commands.domain.core.values.user.FirstName;
import com.azat4dev.booking.users.commands.domain.core.values.user.FullName;
import com.azat4dev.booking.users.commands.domain.core.values.user.LastName;

public class MapUserDataToDomainImpl implements MapUserDataToDomain {

    @Override
    public User map(UserData userData) throws DomainException {

        return User.dangerouslyMakeFrom(
            UserId.fromUUID(userData.id()),
            userData.createdAt(),
            userData.updatedAt(),
            EmailAddress.dangerMakeWithoutChecks(userData.email()),
            FullName.makeWithoutChecks(
                FirstName.dangerMakeFromStringWithoutCheck(userData.firstName()),
                LastName.dangerMakeFromStringWithoutCheck(userData.lastName())
            ),
            new EncodedPassword(userData.encodedPassword()),
            userData.emailVerificationStatus(),
            userData.photo().map(UserData.PhotoPath::toDomain)
        );
    }
}
