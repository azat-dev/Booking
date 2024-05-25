package com.azat4dev.booking.users.users_commands.domain.handlers.users.photo;

import com.azat4dev.booking.common.presentation.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.events.GeneratedUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.PhotoFileExtension;

public interface GenerateUrlForUploadUserPhoto {

    int MIN_FILE_SIZE = 0;
    int MAX_FILE_SIZE = 5 * 1024 * 1024;

    GeneratedUserPhotoUploadUrl execute(
        IdempotentOperationId operationId,
        UserId userId,
        PhotoFileExtension fileExtension,
        int fileSize
    ) throws ValidationException, Exception.FailedGenerateUserPhotoUploadUrl, Exception.WrongFileSize;

    // Exceptions

    abstract class Exception extends DomainException {
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
