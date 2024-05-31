package com.azat4dev.booking.users.users_commands.application.handlers.photo;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.application.commands.photo.GenerateUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.domain.core.events.GeneratedUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.PhotoFileExtension;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.photo.GenerateUrlForUploadUserPhoto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GenerateUserPhotoUploadUrlHandlerImpl implements GenerateUserPhotoUploadUrlHandler {

    private final GenerateUrlForUploadUserPhoto generateUrlForUploadUserPhoto;

    public GeneratedUserPhotoUploadUrl handle(
        GenerateUserPhotoUploadUrl command
    ) throws ValidationException, Exception.FailedGenerateUserPhotoUploadUrl {

        try {
            final var userId = UserId.checkAndMakeFrom(command.userId());
            final var operationId = IdempotentOperationId.checkAndMakeFrom(command.operationId());
            final var fileExtension = PhotoFileExtension.checkAndMakeFrom(command.fileExtension());
            final var fileSize = command.fileSize();

            return generateUrlForUploadUserPhoto.execute(operationId, userId, fileExtension, fileSize);

        } catch (PhotoFileExtension.InvalidPhotoFileExtensionException e) {
            throw ValidationException.withPath("fileExtension", e);
        } catch (IdempotentOperationId.Exception e) {
            throw ValidationException.withPath("operationId", e);
        } catch (GenerateUrlForUploadUserPhoto.Exception.WrongFileSize e) {
            throw ValidationException.withPath("fileSize", e);
        } catch (GenerateUrlForUploadUserPhoto.Exception.FailedGenerateUserPhotoUploadUrl e) {
            throw new Exception.FailedGenerateUserPhotoUploadUrl();
        } catch (UserId.WrongFormatException e) {
            throw ValidationException.withPath("userId", e);
        }
    }
}
