package com.azat4dev.demobooking.users.users_commands.domain.core.commands;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.Command;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.PhotoFileExtension;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@EqualsAndHashCode
@Getter
public final class GenerateUserPhotoUploadUrl implements Command {

    public static final int MAX_FILE_SIZE = 5 * 1024 * 1024;
    private final UserId userId;
    private final PhotoFileExtension fileExtension;
    private final int fileSize;
    private final IdempotentOperationId operationId;
    private final LocalDateTime requestedAt;


    /**
     * @throws Exception.InvalidFileSize
     */
    public GenerateUserPhotoUploadUrl(
        UserId userId,
        PhotoFileExtension fileExtension,
        int fileSize,
        IdempotentOperationId operationId,
        LocalDateTime requestedAt
    ) throws Exception {

        if (fileSize > MAX_FILE_SIZE) {
            throw new Exception.InvalidFileSize();
        }

        this.userId = userId;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.operationId = operationId;
        this.requestedAt = requestedAt;
    }

    // Exceptions

    public static sealed

    abstract class Exception extends DomainException permits Exception.InvalidFileSize {
        public Exception(String message) {
            super(message);
        }

        public static final class InvalidFileSize extends Exception {
            public InvalidFileSize() {
                super("File size should be less than 5MB");
            }
        }
    }
}

