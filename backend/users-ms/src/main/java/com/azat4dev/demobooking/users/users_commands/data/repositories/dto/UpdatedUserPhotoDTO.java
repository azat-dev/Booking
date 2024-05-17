package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UpdatedUserPhoto;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;

import java.util.Optional;

public record UpdatedUserPhotoDTO(
    String idempotentOperationId,
    String userId,
    UserPhotoPathDTO newPhotoPath,
    UserPhotoPathDTO prevPhotoPath
) implements DomainEventPayloadDTO {

    public static UpdatedUserPhotoDTO fromDomain(UpdatedUserPhoto dm) {
        return new UpdatedUserPhotoDTO(
            dm.idempotentOperationId().toString(),
            dm.userId().toString(),
            UserPhotoPathDTO.fromDomain(dm.newPhotoPath()),
            dm.prevPhotoPath().map(UserPhotoPathDTO::fromDomain).orElse(null)
        );
    }

    public UpdatedUserPhoto toDomain() {
        return new UpdatedUserPhoto(
            IdempotentOperationId.dangerouslyMakeFrom(idempotentOperationId),
            UserId.dangerouslyMakeFrom(userId),
            newPhotoPath.toDomain(),
            prevPhotoPath == null ? Optional.empty() : Optional.of(prevPhotoPath.toDomain())
        );
    }
}
