package com.azat4dev.booking.users.users_commands.domain.core.events;

import com.azat4dev.booking.shared.domain.event.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.PhotoFileExtension;

import java.time.LocalDateTime;

public record FailedGenerateUserPhotoUploadUrl(
    UserId userId,
    PhotoFileExtension fileExtension,
    int fileSize,
    IdempotentOperationId idempotentOperationId
) implements DomainEventPayload {
}
