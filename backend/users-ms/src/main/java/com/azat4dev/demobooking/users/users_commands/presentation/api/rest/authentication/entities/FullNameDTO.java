package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.LastName;

public record FullNameDTO(
    String firstName,
    String lastName
) {

    public static FullNameDTO fromDomain(FullName fullName) {
        return new FullNameDTO(
            fullName.getFirstName().getValue(),
            fullName.getLastName().getValue()
        );
    }

    public FullName toDomain() throws FullName.Exception, FirstName.ValidationException, LastName.ValidationException {
        return new FullName(
            FirstName.checkAndMakeFromString(firstName),
            LastName.checkAndMakeFromString(lastName)
        );
    }
}
