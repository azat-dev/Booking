package com.azat4dev.booking.users.users_queries.presentation.api.rest.dto;

import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.FullNameDTO;
import com.azat4dev.booking.users.users_queries.domain.entities.PersonalUserInfo;

import java.util.Optional;

public record PersonalUserInfoDTO(
    String id,
    String email,
    String emailVerficationStatus,
    FullNameDTO fullName,
    Optional<PhotoPathDTO> photo,
    String emailVerificationStatus
) {

    public static PersonalUserInfoDTO from(PersonalUserInfo user) {
        return new PersonalUserInfoDTO(
            user.id().toString(),
            user.email(),
            user.emailVerificationStatus().name(),
            FullNameDTO.fromDomain(user.fullName()),
            user.photo().map(p -> new PhotoPathDTO(p.url().toString())),
            user.emailVerificationStatus().name()
        );
    }
}
