package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedGenerateUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.PhotoFileExtension;

import java.time.LocalDateTime;

public record FailedGenerateUserPhotoUploadUrlDTO(
    String userId,
    String fileExtension,
    int fileSize,
    String idempotentOperationId,
    LocalDateTime requestedAt
) implements DomainEventPayloadDTO {

    public static FailedGenerateUserPhotoUploadUrlDTO fromDomain(FailedGenerateUserPhotoUploadUrl event) {
        return new FailedGenerateUserPhotoUploadUrlDTO(
            event.userId().value().toString(),
            event.fileExtension().getValue(),
            event.fileSize(),
            event.idempotentOperationId().value().toString(),
            event.requestedAt()
        );
    }

    @Override
    public FailedGenerateUserPhotoUploadUrl toDomain() {
        return new FailedGenerateUserPhotoUploadUrl(
            UserId.fromString(userId),
            PhotoFileExtension.dangerouslyMakeFrom(fileExtension),
            fileSize,
            IdempotentOperationId.makeFromString(idempotentOperationId),
            requestedAt
        );
    }
}
