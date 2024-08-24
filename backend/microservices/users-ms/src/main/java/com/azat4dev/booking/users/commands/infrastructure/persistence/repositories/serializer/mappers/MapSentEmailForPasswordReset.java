package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.SentEmailForPasswordReset;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.usersms.generated.api.bus.dto.usersms.SentEmailForPasswordResetDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapSentEmailForPasswordReset implements MapDomainEvent<SentEmailForPasswordReset, SentEmailForPasswordResetDTO> {

    @Override
    public SentEmailForPasswordResetDTO serialize(SentEmailForPasswordReset dm) {
        return SentEmailForPasswordResetDTO.builder()
            .userId(dm.userId().toString())
            .email(dm.email().getValue())
            .build();
    }

    @Override
    public SentEmailForPasswordReset deserialize(SentEmailForPasswordResetDTO dto) {
        return new SentEmailForPasswordReset(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            EmailAddress.dangerMakeWithoutChecks(dto.getEmail())
        );
    }

    @Override
    public Class<SentEmailForPasswordReset> getOriginalClass() {
        return SentEmailForPasswordReset.class;
    }

    @Override
    public Class<SentEmailForPasswordResetDTO> getSerializedClass() {
        return SentEmailForPasswordResetDTO.class;
    }
}