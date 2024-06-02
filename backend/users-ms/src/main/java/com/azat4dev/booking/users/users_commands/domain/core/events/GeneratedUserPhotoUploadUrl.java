package com.azat4dev.booking.users.users_commands.domain.core.events;

import com.azat4dev.booking.shared.domain.event.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.files.UploadFileFormData;
import com.azat4dev.booking.shared.domain.values.user.UserId;

public record GeneratedUserPhotoUploadUrl(
    UserId userId,
    UploadFileFormData formData
) implements DomainEventPayload {
}
