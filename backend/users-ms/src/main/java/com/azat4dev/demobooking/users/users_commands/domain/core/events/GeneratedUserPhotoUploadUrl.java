package com.azat4dev.demobooking.users.users_commands.domain.core.events;

import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.UploadFileFormData;

public record GeneratedUserPhotoUploadUrl(
    UserId userId,
    UploadFileFormData formData
) implements DomainEventPayload {
}
