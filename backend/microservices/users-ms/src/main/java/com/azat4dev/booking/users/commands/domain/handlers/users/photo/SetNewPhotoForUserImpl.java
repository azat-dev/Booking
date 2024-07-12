package com.azat4dev.booking.users.commands.domain.handlers.users.photo;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObject;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.UploadedFileData;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.commands.domain.core.events.FailedUpdateUserPhoto;
import com.azat4dev.booking.users.commands.domain.handlers.users.Users;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Observed
@Slf4j
@RequiredArgsConstructor
public class SetNewPhotoForUserImpl implements SetNewPhotoForUser {

    private final MediaObjectsBucket mediaObjectsBucket;
    private final Users users;
    private final DomainEventsBus bus;

    public void execute(
        IdempotentOperationId operationId,
        UserId userId,
        UploadedFileData uploadedFileData
    ) throws Exception.UserNotFound, Exception.FailedToSaveUser, Exception.FailedToGetPhoto {

        final MediaObject mediaObject;

        try {
            mediaObject = mediaObjectsBucket.getObject(uploadedFileData.objectName());
            log.atDebug().log("Photo received");
        } catch (Throwable e) {
            log.atError().setCause(e).log("Failed to get photo");
            bus.publish(new FailedUpdateUserPhoto(operationId, userId, uploadedFileData));
            throw new Exception.FailedToGetPhoto();
        }

        try {
            users.updatePhoto(
                userId,
                new UserPhotoPath(mediaObject.bucketName(), mediaObject.objectName())
            );

            log.atInfo().log("User photo updated");
        } catch (Users.Exception.FailedToUpdateUser e) {
            log.atWarn().setCause(e).log("Failed to save user");
            bus.publish(new FailedUpdateUserPhoto(operationId, userId, uploadedFileData));
            throw new Exception.FailedToSaveUser();
        } catch (Users.Exception.UserNotFound e) {
            log.atWarn().setCause(e).log("User not found");
            bus.publish(new FailedUpdateUserPhoto(operationId, userId, uploadedFileData));
            throw new Exception.UserNotFound(userId);
        }
    }
}
