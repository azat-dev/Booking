package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.FailedToSendVerificationEmail;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.usersms.generated.api.bus.dto.usersms.FailedToSendVerificationEmailDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapFailedToSendVerificationEmail implements MapDomainEvent<FailedToSendVerificationEmail, FailedToSendVerificationEmailDTO> {

    @Override
    public FailedToSendVerificationEmailDTO serialize(FailedToSendVerificationEmail dm) {
        return FailedToSendVerificationEmailDTO.builder()
            .userId(dm.userId().toString())
            .email(dm.email().getValue())
            .attempts(dm.attempts())
            .build();
    }

    @Override
    public FailedToSendVerificationEmail deserialize(FailedToSendVerificationEmailDTO dto) {
        return new FailedToSendVerificationEmail(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            EmailAddress.dangerMakeWithoutChecks(dto.getEmail()),
            dto.getAttempts()
        );
    }

    @Override
    public Class<FailedToSendVerificationEmail> getOriginalClass() {
        return FailedToSendVerificationEmail.class;
    }

    @Override
    public Class<FailedToSendVerificationEmailDTO> getSerializedClass() {
        return FailedToSendVerificationEmailDTO.class;
    }
}
