package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.demobooking.users.users_commands.domain.entities.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.entities.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.entities.LastName;

public record FullNameDTO(
    String firstName,
    String lastName
) {

    public static FullNameDTO fromDomain(FullName fullName) {
        return new FullNameDTO(
            fullName.firstName().getValue(),
            fullName.lastName().getValue()
        );
    }

    public FullName toDomain() throws FullName.ValidationException, FirstName.ValidationException, LastName.ValidationException {
        return new FullName(
            FirstName.checkAndMakeFromString(firstName),
            LastName.checkAndMakeFromString(lastName)
        );
    }
}
