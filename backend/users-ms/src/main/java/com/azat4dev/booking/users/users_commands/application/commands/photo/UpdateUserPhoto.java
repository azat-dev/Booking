package com.azat4dev.booking.users.users_commands.application.commands.photo;

import com.azat4dev.booking.shared.application.events.ApplicationCommand;
import com.azat4dev.booking.shared.domain.values.user.UserId;

public record UpdateUserPhoto(
    UserId userId,
    String operationId,
    UploadedFileData uploadedFileData
) implements ApplicationCommand {

    public record UploadedFileData(
        String bucketName,
        String objectName
    ) {
    }
}