package com.azat4dev.booking.users.commands.application.commands.photo;

import com.azat4dev.booking.shared.application.events.ApplicationCommand;

public record GenerateUserPhotoUploadUrl(
    String operationId,
    String userId,
    String fileExtension,
    int fileSize
) implements ApplicationCommand {
}

