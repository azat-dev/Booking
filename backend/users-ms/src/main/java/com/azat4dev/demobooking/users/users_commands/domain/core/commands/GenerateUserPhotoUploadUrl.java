package com.azat4dev.demobooking.users.users_commands.domain.core.commands;

import com.azat4dev.demobooking.common.domain.event.Command;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.PhotoFileExtension;

import java.time.LocalDateTime;

public record GenerateUserPhotoUploadUrl(
    UserId userId, PhotoFileExtension fileExtension,
    int fileSize,
    IdempotentOperationId operationId,
    LocalDateTime requestedAt
) implements Command {

    public static final int MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * @throws InvalidFileSizeException
     */
    public GenerateUserPhotoUploadUrl {

        if (fileSize > MAX_FILE_SIZE) {
            throw new InvalidFileSizeException();
        }
    }

    // Exceptions

    public static final class InvalidFileSizeException extends IllegalArgumentException {
        public InvalidFileSizeException() {
            super("File size should be less than 5MB");
        }
    }
}

