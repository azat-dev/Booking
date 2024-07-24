package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.shared.data.serializers.MapDomainEvent;
import com.azat4dev.booking.usersms.generated.events.dto.SendVerificationEmailDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapSendVerificationEmail implements MapDomainEvent<SendVerificationEmail, SendVerificationEmailDTO> {

    private final MapFullName mapFullName;

    @Override
    public SendVerificationEmailDTO serialize(SendVerificationEmail dm) {
        return SendVerificationEmailDTO.builder()
            .userId(dm.userId().toString())
            .email(dm.email().getValue())
            .fullName(mapFullName.serialize(dm.fullName()))
            .attempt(dm.attempt())
            .build();
    }

    @Override
    public SendVerificationEmail deserialize(SendVerificationEmailDTO dto) {
        return new SendVerificationEmail(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            EmailAddress.dangerMakeWithoutChecks(dto.getEmail()),
            mapFullName.deserialize(dto.getFullName()),
            dto.getAttempt()
        );
    }

    @Override
    public Class<SendVerificationEmail> getOriginalClass() {
        return SendVerificationEmail.class;
    }

    @Override
    public Class<SendVerificationEmailDTO> getSerializedClass() {
        return SendVerificationEmailDTO.class;
    }
}