package com.azat4dev.demobooking.users.users_commands.domain.core.events;

import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.PhotoFileExtension;

import java.time.LocalDateTime;

public record FailedGenerateUserPhotoUploadUrl(
    UserId userId,
    PhotoFileExtension fileExtension,
    int fileSize,
    IdempotentOperationId idempotentOperationId,
    LocalDateTime requestedAt
) implements DomainEventPayload {
}
