package com.azat4dev.booking.users.users_commands.domain.core.events;

import com.azat4dev.booking.shared.domain.event.DomainEventPayload;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.files.UploadFileFormData;

public record GeneratedUserPhotoUploadUrl(
    UserId userId,
    UploadFileFormData formData
) implements DomainEventPayload {
}
