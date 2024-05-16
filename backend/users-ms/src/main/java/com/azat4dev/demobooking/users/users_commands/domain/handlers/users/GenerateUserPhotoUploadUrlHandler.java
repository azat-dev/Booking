package com.azat4dev.demobooking.users.users_commands.domain.handlers.users;

import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.GenerateUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.GeneratedUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectsBucket;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GenerateUserPhotoUploadUrlHandler {

    private final int expireInSeconds;
    private final GenerateUserPhotoObjectName generateUserPhotoObjectName;
    private final MediaObjectsBucket usersPhotoBucket;
    private final DomainEventsBus bus;

    public GeneratedUserPhotoUploadUrl handle(GenerateUserPhotoUploadUrl command) {

        try {

            final var objectName = generateUserPhotoObjectName.execute(command.userId());

            final var url = usersPhotoBucket.generateUploadUrl(
                objectName,
                command.fileExtension(),
                expireInSeconds
            );

            final var event = new GeneratedUserPhotoUploadUrl(command.userId(), url);
            bus.publish(event);
            return event;

        } catch (Exception e) {
            throw new FailedGenerateUserPhotoUploadUrlException();
        }
    }

    // Exceptions

    public static final class FailedGenerateUserPhotoUploadUrlException extends RuntimeException {
        public FailedGenerateUserPhotoUploadUrlException() {
            super("Failed to generate presigned URL for uploading user photo");
        }
    }
}
