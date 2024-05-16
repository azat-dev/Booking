package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.GenerateUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.GeneratedUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.PhotoFileExtension;

import java.net.MalformedURLException;
import java.net.URL;
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
            event.userId().value().toString(),
            event.fileExtension().getValue(),
            event.fileSize(),
            event.operationId().value().toString(),
            event.requestedAt()
        );
    }

    public GenerateUserPhotoUploadUrl toDomain() {
        return new GenerateUserPhotoUploadUrl(
            UserId.fromString(userId),
            PhotoFileExtension.dangerouslyMakeFrom(fileExtension),
            fileSize,
            IdempotentOperationId.makeFromString(operationId),
            requestedAt
        );
    }
}
