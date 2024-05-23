package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.booking.common.presentation.ValidationException;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.commands.UpdateUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.files.UploadedFileData;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectName;

public record UpdateUserPhotoRequest(
    UserId currentUserId,
    UpdateUserPhotoRequestBody requestBody
) {

    // Exceptions
    private static ValidationException mapException(BucketName.Exception e) {
        return ValidationException.withPath("uploadedFile.bucketName", e);
    }

    private static ValidationException mapException(IdempotentOperationId.Exception e) {
        return ValidationException.withPath("idempotentOperationId", e);
    }

    public UpdateUserPhoto toCommand() {
        try {
            return new UpdateUserPhoto(
                IdempotentOperationId.checkAndMakeFrom(requestBody.idempotentOperationId()),
                currentUserId,
                new UploadedFileData(
                    BucketName.checkAndMake(requestBody.uploadedFile().bucketName()),
                    MediaObjectName.checkAndMakeFrom(requestBody.uploadedFile().objectName())
                )
            );
        } catch (IdempotentOperationId.Exception e) {
            throw mapException(e);
        } catch (BucketName.Exception e) {
            throw mapException(e);
        }
    }
}
