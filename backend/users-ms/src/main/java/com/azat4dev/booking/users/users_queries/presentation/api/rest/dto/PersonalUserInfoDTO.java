package com.azat4dev.booking.users.users_queries.presentation.api.rest.dto;

import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.FullNameDTO;
import com.azat4dev.booking.users.users_queries.domain.entities.PersonalUserInfo;

import java.util.Optional;

public record PersonalUserInfoDTO(
    String id,
    String email,
    FullNameDTO fullName,
    Optional<PhotoPathDTO> photo
) {

    public static PersonalUserInfoDTO from(PersonalUserInfo user) {
        return new PersonalUserInfoDTO(
            user.id().toString(),
            user.email(),
            FullNameDTO.fromDomain(user.fullName()),
            user.photo().map(p -> new PhotoPathDTO(p.url().toString()))
        );
    }
}
