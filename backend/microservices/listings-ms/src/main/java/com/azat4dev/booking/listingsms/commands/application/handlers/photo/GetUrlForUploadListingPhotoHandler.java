package com.azat4dev.booking.listingsms.commands.application.handlers.photo;

import com.azat4dev.booking.listingsms.commands.application.commands.GetUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.events.GeneratedUrlForUploadListingPhoto;
import com.azat4dev.booking.shared.domain.DomainException;

public interface GetUrlForUploadListingPhotoHandler {

    GeneratedUrlForUploadListingPhoto handle(GetUrlForUploadListingPhoto command) throws Exception.FailedGenerate;

    abstract class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static final class FailedGenerate extends Exception {
            public FailedGenerate() {
                super("Failed to generate presigned URL for uploading listing photo");
            }
        }
    }
}
