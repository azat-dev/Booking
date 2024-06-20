package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.users.commands.infrastructure.entities.UserData;
import com.azat4dev.booking.users.commands.domain.core.entities.User;

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
