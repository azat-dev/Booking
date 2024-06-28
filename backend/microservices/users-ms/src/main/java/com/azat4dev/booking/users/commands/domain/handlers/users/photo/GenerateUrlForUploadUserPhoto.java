package com.azat4dev.booking.users.commands.domain.handlers.users.photo;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.GeneratedUserPhotoUploadUrl;

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
        protected Exception(String message) {
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
