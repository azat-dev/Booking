package com.azat4dev.booking.users.users_commands.domain.handlers.users.photo;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.shared.domain.values.files.UploadedFileData;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;


public interface SetNewPhotoForUser {

    void execute(
        IdempotentOperationId operationId,
        UserId userId,
        UploadedFileData uploadedFileData
    ) throws Exception.UserNotFound, Exception.FailedToSaveUser, Exception.FailedToGetPhoto;

    abstract class Exception extends DomainException {
        Exception(String message) {
            super(message);
        }

        public static final class UserNotFound extends Exception {
            public UserNotFound(UserId userId) {
                super("User not found: " + userId);
            }
        }

        public static final class FailedToSaveUser extends Exception {
            public FailedToSaveUser() {
                super("Failed to save user");
            }
        }

        public static final class FailedToGetPhoto extends Exception {
            public FailedToGetPhoto() {
                super("Failed to get photo");
            }
        }
    }
}
