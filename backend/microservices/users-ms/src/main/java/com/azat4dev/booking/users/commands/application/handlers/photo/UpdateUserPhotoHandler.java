package com.azat4dev.booking.users.commands.application.handlers.photo;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.users.commands.application.commands.photo.UpdateUserPhoto;

public interface UpdateUserPhotoHandler {

    void handle(UpdateUserPhoto command) throws ValidationException, Exception.FailedToAttachPhoto;

    // Exceptions

    abstract class Exception extends DomainException {

        public Exception(String message) {
            super(message);
        }

        public static final class FailedToAttachPhoto extends Exception {
            public FailedToAttachPhoto() {
                super("Failed to attach image");
            }
        }
    }
}
