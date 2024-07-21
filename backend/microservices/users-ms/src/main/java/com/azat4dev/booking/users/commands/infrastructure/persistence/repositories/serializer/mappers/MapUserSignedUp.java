package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.UserSignedUp;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.commands.domain.core.values.user.FullName;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.shared.data.serializers.Mapper;
import com.azat4dev.booking.usersms.generated.events.dto.FullNameDTO;
import com.azat4dev.booking.usersms.generated.events.dto.UserSignedUpDTO;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public final class MapUserSignedUp implements MapPayload<UserSignedUp, UserSignedUpDTO> {

    private final Mapper<FullName, FullNameDTO> mapFullName;
    private final Mapper<LocalDateTime, String> mapDateTime;

    @Override
    public UserSignedUpDTO toDTO(UserSignedUp dm) {
        return UserSignedUpDTO.builder()
            .createdAt(mapDateTime.toDTO(dm.createdAt()))
            .userId(dm.userId().toString())
            .email(dm.email().getValue())
            .fullName(mapFullName.toDTO(dm.fullName()))
            .emailVerificationStatus(dm.emailVerificationStatus().name())
            .build();
    }

    @Override
    public UserSignedUp toDomain(UserSignedUpDTO inst) {
        return new UserSignedUp(
            mapDateTime.toDomain(inst.getCreatedAt()),
            UserId.dangerouslyMakeFrom(inst.getUserId()),
            mapFullName.toDomain(inst.getFullName()),
            EmailAddress.dangerMakeWithoutChecks(inst.getEmail()),
            EmailVerificationStatus.valueOf(inst.getEmailVerificationStatus())
        );
    }

    @Override
    public Class<UserSignedUp> getDomainClass() {
        return UserSignedUp.class;
    }

    @Override
    public Class<UserSignedUpDTO> getDTOClass() {
        return UserSignedUpDTO.class;
    }
}