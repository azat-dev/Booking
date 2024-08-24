package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.usersms.generated.api.bus.dto.usersms.UserVerifiedEmailDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapUserVerifiedEmail implements MapDomainEvent<UserVerifiedEmail, UserVerifiedEmailDTO> {

    @Override
    public UserVerifiedEmailDTO serialize(UserVerifiedEmail dm) {
        return UserVerifiedEmailDTO.builder()
            .userId(dm.userId().toString())
            .email(dm.emailAddress().getValue())
            .build();
    }

    @Override
    public UserVerifiedEmail deserialize(UserVerifiedEmailDTO dto) {
        return new UserVerifiedEmail(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            EmailAddress.dangerMakeWithoutChecks(dto.getEmail())
        );
    }

    @Override
    public Class<UserVerifiedEmail> getOriginalClass() {
        return UserVerifiedEmail.class;
    }

    @Override
    public Class<UserVerifiedEmailDTO> getSerializedClass() {
        return UserVerifiedEmailDTO.class;
    }
}