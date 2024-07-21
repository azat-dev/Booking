package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.SentEmailForPasswordReset;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.usersms.generated.events.dto.SentEmailForPasswordResetDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapSentEmailForPasswordReset implements MapPayload<SentEmailForPasswordReset, SentEmailForPasswordResetDTO> {

    @Override
    public SentEmailForPasswordResetDTO toDTO(SentEmailForPasswordReset dm) {
        return SentEmailForPasswordResetDTO.builder()
            .userId(dm.userId().toString())
            .email(dm.email().getValue())
            .build();
    }

    @Override
    public SentEmailForPasswordReset toDomain(SentEmailForPasswordResetDTO dto) {
        return new SentEmailForPasswordReset(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            EmailAddress.dangerMakeWithoutChecks(dto.getEmail())
        );
    }

    @Override
    public Class<SentEmailForPasswordReset> getDomainClass() {
        return SentEmailForPasswordReset.class;
    }

    @Override
    public Class<SentEmailForPasswordResetDTO> getDTOClass() {
        return SentEmailForPasswordResetDTO.class;
    }
}