package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.LastName;

import java.io.Serializable;

public record FullNameDTO(
    String firstName,
    String lastName
) implements Serializable {

    public FullNameDTO(FullName fullName) {
        this(fullName.getFirstName().getValue(), fullName.getLastName().getValue());
    }

    public FullName toDomain() {
        return new FullName(
            FirstName.dangerMakeFromStringWithoutCheck(firstName),
            LastName.checkAndMakeFromString(lastName)
        );
    }
}
