package com.azat4dev.booking.users.users_queries.presentation.api.rest.resources.mappers;

import com.azat4dev.booking.users.users_queries.domain.entities.PersonalUserInfo;
import com.azat4dev.booking.usersms.generated.server.model.EmailVerificationStatusDTO;
import com.azat4dev.booking.usersms.generated.server.model.FullNameDTO;
import com.azat4dev.booking.usersms.generated.server.model.PersonalUserInfoDTO;
import com.azat4dev.booking.usersms.generated.server.model.PhotoPathDTO;

public final class MapPersonalUserInfoToDTOImpl implements MapPersonalUserInfoToDTO {
    @Override
    public PersonalUserInfoDTO map(PersonalUserInfo userInfo) {
        final var fullName = userInfo.fullName();

        final var fullNameDTO = FullNameDTO.builder()
            .firstName(fullName.getFirstName().getValue())
            .lastName(fullName.getLastName().getValue())
            .build();

        final var emailVerificationStatus = EmailVerificationStatusDTO.valueOf(userInfo.emailVerificationStatus().name());

        final var photo = userInfo.photo()
            .map(p -> new PhotoPathDTO(p.url().toString()))
            .orElse(null);

        return PersonalUserInfoDTO.builder()
            .id(userInfo.id().value())
            .email(userInfo.email())
            .emailVerificationStatus(emailVerificationStatus)
            .fullName(fullNameDTO)
            .photo(photo).build();
    }
}