package com.azat4dev.demobooking.users.users_queries.presentation.api.rest.dto;

import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.FullNameDTO;
import com.azat4dev.demobooking.users.users_queries.domain.entities.PersonalUserInfo;

public record PersonalUserInfoDTO(
    String id,
    String email,
    FullNameDTO fullName
) {

    public static PersonalUserInfoDTO from(PersonalUserInfo user) {
        return new PersonalUserInfoDTO(
            user.id().toString(),
            user.email(),
            FullNameDTO.fromDomain(user.fullName())
        );
    }
}
