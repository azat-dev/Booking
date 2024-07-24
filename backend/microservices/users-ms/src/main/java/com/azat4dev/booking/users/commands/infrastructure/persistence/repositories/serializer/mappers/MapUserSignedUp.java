package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.UserSignedUp;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.commands.domain.core.values.user.FullName;
import com.azat4dev.booking.shared.data.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.data.serializers.Serializer;
import com.azat4dev.booking.usersms.generated.events.dto.FullNameDTO;
import com.azat4dev.booking.usersms.generated.events.dto.UserSignedUpDTO;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public final class MapUserSignedUp implements MapDomainEvent<UserSignedUp, UserSignedUpDTO> {

    private final Serializer<FullName, FullNameDTO> mapFullName;
    private final Serializer<LocalDateTime, String> mapDateTime;

    @Override
    public UserSignedUpDTO serialize(UserSignedUp dm) {
        return UserSignedUpDTO.builder()
            .createdAt(mapDateTime.serialize(dm.createdAt()))
            .userId(dm.userId().toString())
            .email(dm.email().getValue())
            .fullName(mapFullName.serialize(dm.fullName()))
            .emailVerificationStatus(dm.emailVerificationStatus().name())
            .build();
    }

    @Override
    public UserSignedUp deserialize(UserSignedUpDTO inst) {
        return new UserSignedUp(
            mapDateTime.deserialize(inst.getCreatedAt()),
            UserId.dangerouslyMakeFrom(inst.getUserId()),
            mapFullName.deserialize(inst.getFullName()),
            EmailAddress.dangerMakeWithoutChecks(inst.getEmail()),
            EmailVerificationStatus.valueOf(inst.getEmailVerificationStatus())
        );
    }

    @Override
    public Class<UserSignedUp> getOriginalClass() {
        return UserSignedUp.class;
    }

    @Override
    public Class<UserSignedUpDTO> getSerializedClass() {
        return UserSignedUpDTO.class;
    }
}