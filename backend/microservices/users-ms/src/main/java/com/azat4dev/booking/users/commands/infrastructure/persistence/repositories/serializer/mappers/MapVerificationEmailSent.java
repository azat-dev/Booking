package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.VerificationEmailSent;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.usersms.generated.api.bus.dto.usersms.VerificationEmailSentDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapVerificationEmailSent implements MapDomainEvent<VerificationEmailSent, VerificationEmailSentDTO> {

    @Override
    public VerificationEmailSentDTO serialize(VerificationEmailSent dm) {
        return VerificationEmailSentDTO.builder()
            .userId(dm.userId().toString())
            .email(dm.emailAddress().getValue())
            .build();
    }

    @Override
    public VerificationEmailSent deserialize(VerificationEmailSentDTO dto) {
        return new VerificationEmailSent(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            EmailAddress.dangerMakeWithoutChecks(dto.getEmail())
        );
    }

    @Override
    public Class<VerificationEmailSent> getOriginalClass() {
        return VerificationEmailSent.class;
    }

    @Override
    public Class<VerificationEmailSentDTO> getSerializedClass() {
        return VerificationEmailSentDTO.class;
    }
}