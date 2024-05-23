package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.booking.common.presentation.ValidationException;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.commands.GenerateUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.PhotoFileExtension;

public record GenerateUploadUserPhotoUrlRequest(
    String idempotentOperationId,
    String fileName,
    String fileExtension,
    int fileSize
) {

    public GenerateUserPhotoUploadUrl toCommand(UserId userId, TimeProvider timeProvider) {
        try {

            return new GenerateUserPhotoUploadUrl(
                userId,
                PhotoFileExtension.checkAndMakeFrom(fileExtension),
                fileSize,
                IdempotentOperationId.checkAndMakeFrom(idempotentOperationId),
                timeProvider.currentTime()
            );
        } catch (PhotoFileExtension.InvalidPhotoFileExtensionException e) {
            throw ValidationException.withPath("fileExtension", e);
        } catch (IdempotentOperationId.Exception e) {
            throw ValidationException.withPath("idempotentOperationId", e);
        } catch (GenerateUserPhotoUploadUrl.Exception.InvalidFileSize e) {
            throw ValidationException.withPath("fileSize", e);
        } catch (GenerateUserPhotoUploadUrl.Exception e) {
            throw new RuntimeException(e);
        }
    }
}
