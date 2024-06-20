package com.azat4dev.booking.users.commands.domain.core.events;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;

public record FailedGenerateUserPhotoUploadUrl(
    UserId userId,
    PhotoFileExtension fileExtension,
    int fileSize,
    IdempotentOperationId operationId
) implements DomainEventPayload {
}
