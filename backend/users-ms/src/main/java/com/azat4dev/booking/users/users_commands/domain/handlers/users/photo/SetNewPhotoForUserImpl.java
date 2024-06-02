package com.azat4dev.booking.users.users_commands.domain.handlers.users.photo;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObject;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.UploadedFileData;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.users_commands.domain.core.events.FailedUpdateUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.Users;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public final class SetNewPhotoForUserImpl implements SetNewPhotoForUser {

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
        } catch (Throwable e) {
            bus.publish(new FailedUpdateUserPhoto(operationId, userId, uploadedFileData));
            throw new Exception.FailedToGetPhoto();
        }

        try {
            users.updatePhoto(
                userId,
                new UserPhotoPath(mediaObject.bucketName(), mediaObject.objectName())
            );
        } catch (Users.Exception.FailedToUpdateUser e) {
            bus.publish(new FailedUpdateUserPhoto(operationId, userId, uploadedFileData));
            throw new Exception.FailedToSaveUser();
        } catch (Users.Exception.UserNotFound e) {
            bus.publish(new FailedUpdateUserPhoto(operationId, userId, uploadedFileData));
            throw new Exception.UserNotFound(userId);
        }
    }
}
