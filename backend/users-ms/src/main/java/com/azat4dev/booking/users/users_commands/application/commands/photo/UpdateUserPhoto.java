package com.azat4dev.booking.users.users_commands.application.commands.photo;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.shared.domain.event.Command;

public record UpdateUserPhoto(
    UserId userId,
    String operationId,
    UploadedFileData uploadedFileData
) implements Command {

    public record UploadedFileData(
        String bucketName,
        String objectName
    ) {
    }
}