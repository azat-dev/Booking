package com.azat4dev.demobooking.users.users_commands.domain.handlers.users;

import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.GenerateUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.GeneratedUserPhotoUploadUrl;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectsBucket;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public final class GenerateUserPhotoUploadUrlHandler {

    private final MediaObjectsBucket mediaObjectsBucket;
    private final DomainEventsBus bus;

    public GeneratedUserPhotoUploadUrl handle(GenerateUserPhotoUploadUrl command) {

        try {


//            bus.publish(new GeneratedUserPhotoUploadUrl(command.userId(), url));

        } catch (Exception e) {
            throw new FailedGenerateUserPhotoUploadUrlException();
        }

//        bus.publish(
//            successEvent
//        );
//
//        return successEvent;

        return null;
    }

    // Exceptions

    public static final class FailedGenerateUserPhotoUploadUrlException extends RuntimeException {
        public FailedGenerateUserPhotoUploadUrlException() {
            super("Failed to generate presigned URL for uploading user photo");
        }
    }
}
