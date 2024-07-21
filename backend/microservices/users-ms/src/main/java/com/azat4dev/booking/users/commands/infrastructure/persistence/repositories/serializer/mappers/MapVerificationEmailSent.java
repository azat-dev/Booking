package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.VerificationEmailSent;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.usersms.generated.events.dto.VerificationEmailSentDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapVerificationEmailSent implements MapPayload<VerificationEmailSent, VerificationEmailSentDTO> {

    @Override
    public VerificationEmailSentDTO toDTO(VerificationEmailSent dm) {
        return VerificationEmailSentDTO.builder()
            .userId(dm.userId().toString())
            .email(dm.emailAddress().getValue())
            .build();
    }

    @Override
    public VerificationEmailSent toDomain(VerificationEmailSentDTO dto) {
        return new VerificationEmailSent(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            EmailAddress.dangerMakeWithoutChecks(dto.getEmail())
        );
    }

    @Override
    public Class<VerificationEmailSent> getDomainClass() {
        return VerificationEmailSent.class;
    }

    @Override
    public Class<VerificationEmailSentDTO> getDTOClass() {
        return VerificationEmailSentDTO.class;
    }
}