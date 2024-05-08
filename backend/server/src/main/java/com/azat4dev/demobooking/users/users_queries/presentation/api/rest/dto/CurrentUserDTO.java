package com.azat4dev.demobooking.users.users_queries.presentation.api.rest.dto;

import com.azat4dev.demobooking.users.users_commands.domain.entities.User;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.FullNameDTO;

public record CurrentUserDTO(
    String id,
    String email,
    FullNameDTO fullName
) {

    public static CurrentUserDTO from(User user) {
        return new CurrentUserDTO(
            user.id().toString(),
            user.email().getValue(),
            FullNameDTO.fromDomain(user.fullName())
        );
    }
}
