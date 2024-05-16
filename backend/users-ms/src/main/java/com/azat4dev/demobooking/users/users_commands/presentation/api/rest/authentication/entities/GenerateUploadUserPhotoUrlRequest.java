package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.GenerateUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.PhotoFileExtension;

public record GenerateUploadUserPhotoUrlRequest(
    String idempotentOperationId,
    String fileName,
    String fileExtension,
    int fileSize
) {

    public GenerateUserPhotoUploadUrl toCommand(UserId userId, TimeProvider timeProvider) {
        return new GenerateUserPhotoUploadUrl(
            userId,
            PhotoFileExtension.checkAndMakeFrom(fileExtension),
            fileSize,
            IdempotentOperationId.makeFromString(idempotentOperationId),
            timeProvider.currentTime()
        );
    }
}
