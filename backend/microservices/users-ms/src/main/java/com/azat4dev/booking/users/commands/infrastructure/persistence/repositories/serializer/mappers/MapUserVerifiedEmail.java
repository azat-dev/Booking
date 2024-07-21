package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.usersms.generated.events.dto.UserVerifiedEmailDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapUserVerifiedEmail implements MapPayload<UserVerifiedEmail, UserVerifiedEmailDTO> {

    @Override
    public UserVerifiedEmailDTO toDTO(UserVerifiedEmail dm) {
        return UserVerifiedEmailDTO.builder()
            .userId(dm.userId().toString())
            .email(dm.emailAddress().getValue())
            .build();
    }

    @Override
    public UserVerifiedEmail toDomain(UserVerifiedEmailDTO dto) {
        return new UserVerifiedEmail(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            EmailAddress.dangerMakeWithoutChecks(dto.getEmail())
        );
    }

    @Override
    public Class<UserVerifiedEmail> getDomainClass() {
        return UserVerifiedEmail.class;
    }

    @Override
    public Class<UserVerifiedEmailDTO> getDTOClass() {
        return UserVerifiedEmailDTO.class;
    }
}