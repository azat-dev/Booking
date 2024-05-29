package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.data.DomainEventPayloadDTO;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.events.UpdatedUserPhoto;

import java.util.Optional;

public record UpdatedUserPhotoDTO(
    String userId,
    UserPhotoPathDTO newPhotoPath,
    UserPhotoPathDTO prevPhotoPath
) implements DomainEventPayloadDTO {

    public static UpdatedUserPhotoDTO fromDomain(UpdatedUserPhoto dm) {
        return new UpdatedUserPhotoDTO(
            dm.userId().toString(),
            UserPhotoPathDTO.fromDomain(dm.newPhotoPath()),
            dm.prevPhotoPath().map(UserPhotoPathDTO::fromDomain).orElse(null)
        );
    }

    public UpdatedUserPhoto toDomain() {
        return new UpdatedUserPhoto(
            UserId.dangerouslyMakeFrom(userId),
            newPhotoPath.toDomain(),
            prevPhotoPath == null ? Optional.empty() : Optional.of(prevPhotoPath.toDomain())
        );
    }
}
