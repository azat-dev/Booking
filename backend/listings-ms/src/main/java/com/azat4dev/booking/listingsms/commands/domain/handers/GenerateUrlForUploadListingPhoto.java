package com.azat4dev.booking.listingsms.commands.domain.handers;

import com.azat4dev.booking.listingsms.commands.domain.events.GeneratedUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;

public interface GenerateUrlForUploadListingPhoto {

    int MIN_FILE_SIZE = 0;
    int MAX_FILE_SIZE = 5 * 1024 * 1024;

    GeneratedUrlForUploadListingPhoto execute(
        IdempotentOperationId operationId,
        UserId userId,
        ListingId listingId,
        PhotoFileExtension fileExtension,
        int fileSize
    ) throws ValidationException, Exception.FailedGenerate, Exception.WrongFileSize;

    // Exceptions

    abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static final class FailedGenerate extends Exception {
            public FailedGenerate() {
                super("Failed to generate presigned URL for uploading listing photo");
            }
        }

        public static final class WrongFileSize extends Exception {
            public WrongFileSize() {
                super("File size must be at least " + MIN_FILE_SIZE + " bytes and not exceed " + MAX_FILE_SIZE + " bytes");
            }
        }
    }
}
