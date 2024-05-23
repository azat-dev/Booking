package com.azat4dev.booking.users.users_queries.presentation.api.rest.dto;

import com.azat4dev.booking.users.users_commands.domain.core.values.user.FullName;

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
}
