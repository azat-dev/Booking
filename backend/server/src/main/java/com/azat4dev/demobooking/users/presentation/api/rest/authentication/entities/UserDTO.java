package com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities;

import com.azat4dev.demobooking.users.domain.entities.User;

public record UserDTO(
    String id,
    String email,
    FullNameDTO fullName
) {

    public static UserDTO from(User user) {
        return new UserDTO(
            user.id().toString(),
            user.email().getValue(),
            FullNameDTO.fromDomain(user.fullName())
        );
    }
}
