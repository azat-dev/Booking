package com.azat4dev.demobooking.users.users_queries.presentation.api.rest.dto;

import com.azat4dev.demobooking.users.users_commands.domain.entities.FullName;

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
