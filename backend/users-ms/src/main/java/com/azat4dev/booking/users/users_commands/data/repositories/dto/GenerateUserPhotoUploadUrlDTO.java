package com.azat4dev.booking.users.users_commands.data.repositories.dto;

import com.azat4dev.booking.shared.data.DomainEventPayloadDTO;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.commands.GenerateUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.PhotoFileExtension;

import java.time.LocalDateTime;

public record GenerateUserPhotoUploadUrlDTO(
    String userId,
    String fileExtension,
    int fileSize,
    String operationId,
    LocalDateTime requestedAt
) implements DomainEventPayloadDTO {

    public static GenerateUserPhotoUploadUrlDTO fromDomain(GenerateUserPhotoUploadUrl event) {
        return new GenerateUserPhotoUploadUrlDTO(
            event.getUserId().value().toString(),
            event.getFileExtension().toString(),
            event.getFileSize(),
            event.getOperationId().value().toString(),
            event.getRequestedAt()
        );
    }

    public GenerateUserPhotoUploadUrl toDomain() {
        try {
            return new GenerateUserPhotoUploadUrl(
                UserId.dangerouslyMakeFrom(userId),
                PhotoFileExtension.dangerouslyMakeFrom(fileExtension),
                fileSize,
                IdempotentOperationId.dangerouslyMakeFrom(operationId),
                requestedAt
            );
        } catch (GenerateUserPhotoUploadUrl.Exception e) {
            throw new RuntimeException(e);
        }
    }
}
