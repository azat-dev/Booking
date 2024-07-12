package com.azat4dev.booking.users.commands.application.handlers.photo;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.application.commands.photo.GenerateUserPhotoUploadUrl;
import com.azat4dev.booking.users.commands.domain.core.events.GeneratedUserPhotoUploadUrl;
import com.azat4dev.booking.users.commands.domain.handlers.users.photo.GenerateUrlForUploadUserPhoto;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Observed
@Slf4j
@RequiredArgsConstructor
public class GenerateUserPhotoUploadUrlHandlerImpl implements GenerateUserPhotoUploadUrlHandler {

    private final GenerateUrlForUploadUserPhoto generateUrlForUploadUserPhoto;

    public GeneratedUserPhotoUploadUrl handle(
        GenerateUserPhotoUploadUrl command
    ) throws ValidationException, Exception.FailedGenerateUserPhotoUploadUrl {

        try {
            final var userId = UserId.checkAndMakeFrom(command.userId());
            final var operationId = IdempotentOperationId.checkAndMakeFrom(command.operationId());
            final var fileExtension = PhotoFileExtension.checkAndMakeFrom(command.fileExtension());
            final var fileSize = command.fileSize();

            final var url = generateUrlForUploadUserPhoto.execute(operationId, userId, fileExtension, fileSize);
            log.atInfo().log("Generated URL for uploading user photo");
            return url;

        } catch (PhotoFileExtension.InvalidPhotoFileExtensionException e) {
            log.atWarn().setCause(e).log("Invalid file extension");
            throw ValidationException.withPath("fileExtension", e);
        } catch (IdempotentOperationId.Exception e) {
            log.atWarn().setCause(e).log("Invalid operation id");
            throw ValidationException.withPath("operationId", e);
        } catch (GenerateUrlForUploadUserPhoto.Exception.WrongFileSize e) {
            log.atError().setCause(e).log("Invalid file size");
            throw ValidationException.withPath("fileSize", e);
        } catch (GenerateUrlForUploadUserPhoto.Exception.FailedGenerateUserPhotoUploadUrl e) {
            log.atError().setCause(e).log("Failed to generate URL for uploading user photo");
            throw new Exception.FailedGenerateUserPhotoUploadUrl();
        } catch (UserId.WrongFormatException e) {
            log.atWarn()
                .addKeyValue("code", e.getCode())
                .addKeyValue("errorMessage", e.getMessage())
                .log("Invalid user ID");
            throw ValidationException.withPath("userId", e);
        }
    }
}
