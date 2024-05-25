package com.azat4dev.booking.users.users_commands.application.commands.photo;

public record GenerateUserPhotoUploadUrl(
    String operationId,
    String userId,
    String fileExtension,
    int fileSize
) {
}

