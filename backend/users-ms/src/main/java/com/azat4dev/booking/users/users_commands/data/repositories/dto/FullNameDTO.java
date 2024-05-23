package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.users.users_commands.domain.core.values.user.FirstName;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.FullName;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.LastName;

import java.io.Serializable;

public record FullNameDTO(
    String firstName,
    String lastName
) implements Serializable {

    public FullNameDTO(FullName fullName) {
        this(fullName.getFirstName().getValue(), fullName.getLastName().getValue());
    }

    public FullName toDomain() {
        try {
            return new FullName(
                FirstName.dangerMakeFromStringWithoutCheck(firstName),
                LastName.dangerMakeFromStringWithoutCheck(lastName)
            );
        } catch (FullName.Exception e) {
            throw new RuntimeException(e);
        }
    }
}
