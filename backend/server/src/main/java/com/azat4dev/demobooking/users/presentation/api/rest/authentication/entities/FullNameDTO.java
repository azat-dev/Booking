package com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities;

import com.azat4dev.demobooking.users.domain.entities.FirstName;
import com.azat4dev.demobooking.users.domain.entities.FullName;
import com.azat4dev.demobooking.users.domain.entities.LastName;

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
            new FirstName(firstName),
            new LastName(lastName)
        );
    }
}
