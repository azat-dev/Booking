package com.azat4dev.booking.users.users_commands.application.handlers.photo;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.UploadedFileData;
import com.azat4dev.booking.users.users_commands.application.commands.photo.UpdateUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.photo.SetNewPhotoForUser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class UpdateUserPhotoHandlerImpl implements UpdateUserPhotoHandler {

    private final SetNewPhotoForUser setNewPhotoForUser;

    @Override
    public void handle(UpdateUserPhoto command) throws Exception.FailedToAttachPhoto, ValidationException {

        final var userId = command.userId();
        try {
            final var uploadedFileData = new UploadedFileData(
                BucketName.checkAndMake(command.uploadedFileData().bucketName()),
                MediaObjectName.checkAndMakeFrom(command.uploadedFileData().objectName())
            );

            final var operationId = IdempotentOperationId.checkAndMakeFrom(command.operationId());

            setNewPhotoForUser.execute(operationId, userId, uploadedFileData);

        } catch (BucketName.Exception e) {
            throw ValidationException.withPath("bucketName", e);
        } catch (MediaObjectName.InvalidMediaObjectNameException e) {
            throw ValidationException.withPath("objectName", e);
        } catch (IdempotentOperationId.Exception e) {
            throw ValidationException.withPath("operationId", e);
        } catch (SetNewPhotoForUser.Exception e) {
            throw new Exception.FailedToAttachPhoto();
        }
    }
}
