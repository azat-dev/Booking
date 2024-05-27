package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.data.DomainEventPayloadDTO;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.events.FailedGenerateUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.PhotoFileExtension;

import java.time.LocalDateTime;

public record FailedGenerateUserPhotoUploadUrlDTO(
    String userId,
    String fileExtension,
    int fileSize,
    String idempotentOperationId
) implements DomainEventPayloadDTO {

    public static FailedGenerateUserPhotoUploadUrlDTO fromDomain(FailedGenerateUserPhotoUploadUrl event) {
        return new FailedGenerateUserPhotoUploadUrlDTO(
            event.userId().value().toString(),
            event.fileExtension().toString(),
            event.fileSize(),
            event.idempotentOperationId().value()
        );
    }

    @Override
    public FailedGenerateUserPhotoUploadUrl toDomain() {
        return new FailedGenerateUserPhotoUploadUrl(
            UserId.dangerouslyMakeFrom(userId),
            PhotoFileExtension.dangerouslyMakeFrom(fileExtension),
            fileSize,
            IdempotentOperationId.dangerouslyMakeFrom(idempotentOperationId)
        );
    }
}
