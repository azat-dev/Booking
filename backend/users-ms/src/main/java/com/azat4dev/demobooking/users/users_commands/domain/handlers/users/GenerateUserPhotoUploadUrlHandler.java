package com.azat4dev.demobooking.users.users_commands.domain.handlers.users;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.GenerateUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedGenerateUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.GeneratedUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectsBucket;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public final class GenerateUserPhotoUploadUrlHandler {

    public static final int MIN_FILE_SIZE = 0;
    public static final int MAX_FILE_SIZE = 5 * 1024 * 1024;

    private final int expireInSeconds;
    private final GenerateUserPhotoObjectName generateUserPhotoObjectName;
    private final MediaObjectsBucket usersPhotoBucket;
    private final DomainEventsBus bus;

    public GeneratedUserPhotoUploadUrl handle(GenerateUserPhotoUploadUrl command) throws Exception {

        if (command.getFileSize() < MIN_FILE_SIZE || command.getFileSize() > MAX_FILE_SIZE) {
            throw new Exception.WrongFileSize();
        }

        try {

            final var objectName = generateUserPhotoObjectName.execute(command.getUserId(), command.getFileExtension());

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

            final var event = new GeneratedUserPhotoUploadUrl(command.getUserId(), formData);
            bus.publish(event);
            return event;

        } catch (Throwable e) {

            bus.publish(new FailedGenerateUserPhotoUploadUrl(
                command.getUserId(),
                command.getFileExtension(),
                command.getFileSize(),
                command.getOperationId(),
                command.getRequestedAt()
            ));
            throw new Exception.FailedGenerateUserPhotoUploadUrl();
        }
    }

    // Exceptions

    public static abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static final class FailedGenerateUserPhotoUploadUrl extends Exception {
            public FailedGenerateUserPhotoUploadUrl() {
                super("Failed to generate presigned URL for uploading user photo");
            }
        }

        public static final class WrongFileSize extends Exception {
            public WrongFileSize() {
                super("File size must be at least " + MIN_FILE_SIZE + " bytes and not exceed " + MAX_FILE_SIZE + " bytes");
            }
        }
    }
}
