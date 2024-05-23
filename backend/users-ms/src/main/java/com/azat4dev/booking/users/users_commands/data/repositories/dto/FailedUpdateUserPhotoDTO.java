package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.events.FailedUpdateUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;

public record FailedUpdateUserPhotoDTO(
    String idempotentOperationId,
    String userId,
    UploadedFileDataDTO uploadedFileData
) implements DomainEventPayloadDTO {

    public static FailedUpdateUserPhotoDTO fromDomain(FailedUpdateUserPhoto dm) {
        return new FailedUpdateUserPhotoDTO(
            dm.idempotentOperationId().toString(),
            dm.userId().toString(),
            UploadedFileDataDTO.fromDomain(dm.uploadedFileData())
        );
    }

    public FailedUpdateUserPhoto toDomain() {
        return new FailedUpdateUserPhoto(
            IdempotentOperationId.dangerouslyMakeFrom(idempotentOperationId),
            UserId.dangerouslyMakeFrom(userId),
            uploadedFileData.toDomain()
        );
    }
}
