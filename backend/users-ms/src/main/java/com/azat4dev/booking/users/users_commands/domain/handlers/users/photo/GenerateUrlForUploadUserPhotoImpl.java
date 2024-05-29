package com.azat4dev.booking.users.users_commands.domain.handlers.users.photo;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.shared.domain.event.DomainEventsBus;
import com.azat4dev.booking.users.users_commands.domain.core.events.FailedGenerateUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.domain.core.events.GeneratedUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.PhotoFileExtension;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectsBucket;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public final class GenerateUrlForUploadUserPhotoImpl implements GenerateUrlForUploadUserPhoto {

    private final int expireInSeconds;
    private final GenerateUserPhotoObjectName generateUserPhotoObjectName;
    private final MediaObjectsBucket usersPhotoBucket;
    private final DomainEventsBus bus;

    @Override
    public GeneratedUserPhotoUploadUrl execute(
        IdempotentOperationId operationId,
        UserId userId,
        PhotoFileExtension fileExtension,
        int fileSize
    ) throws ValidationException, Exception.FailedGenerateUserPhotoUploadUrl, Exception.WrongFileSize {

        final Runnable publishFailedEvent = () -> {
            bus.publish(new FailedGenerateUserPhotoUploadUrl(
                userId,
                fileExtension,
                fileSize,
                operationId
            ));
        };

        if (fileSize < MIN_FILE_SIZE || fileSize > MAX_FILE_SIZE) {
            publishFailedEvent.run();
            throw new Exception.WrongFileSize();
        }

        try {

            final var objectName = generateUserPhotoObjectName.execute(userId, fileExtension);

            final var formData = usersPhotoBucket.generateUploadFormData(
                objectName,
                expireInSeconds,
                new MediaObjectsBucket.Policy(
                    Optional.of(new MediaObjectsBucket.FileSizeRange(0, MAX_FILE_SIZE)),
                    new MediaObjectsBucket.Condition[]{
                        new MediaObjectsBucket.Condition(
                            "Content-Type",
                            MediaObjectsBucket.ConditionType.STARTS_WITH,
                            "image/"
                        )
                    }
                )
            );

            final var event = new GeneratedUserPhotoUploadUrl(userId, formData);
            bus.publish(event);
            return event;

        } catch (Throwable e) {

            publishFailedEvent.run();
            throw new Exception.FailedGenerateUserPhotoUploadUrl();
        }
    }
}
