package com.azat4dev.booking.users.commands.domain.core.commands;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@EqualsAndHashCode
@Getter
public final class GenerateUserPhotoUploadUrl implements Command {

    public static final int MAX_FILE_SIZE = 5 * 1024 * 1024;
    private final UserId userId;
    private final PhotoFileExtension fileExtension;
    private final int fileSize;
    private final IdempotentOperationId operationId;
    private final LocalDateTime requestedAt;

    public GenerateUserPhotoUploadUrl(
        UserId userId,
        PhotoFileExtension fileExtension,
        int fileSize,
        IdempotentOperationId operationId,
        LocalDateTime requestedAt
    ) throws Exception {

        if (fileSize > MAX_FILE_SIZE) {
            log.atError().log("File size should be less than 5MB");
            throw new Exception.InvalidFileSize();
        }

        this.userId = userId;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.operationId = operationId;
        this.requestedAt = requestedAt;
    }

    // Exceptions

    public abstract static sealed class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static final class InvalidFileSize extends Exception {
            public InvalidFileSize() {
                super("File size should be less than 5MB");
            }
        }
    }
}

