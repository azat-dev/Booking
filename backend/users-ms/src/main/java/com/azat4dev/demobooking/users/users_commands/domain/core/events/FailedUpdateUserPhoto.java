package com.azat4dev.demobooking.users.users_commands.domain.core.events;

import com.azat4dev.demobooking.common.domain.event.Command;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.UploadedFileData;


public record FailedUpdateUserPhoto(
    IdempotentOperationId idempotentOperationId,
    UserId userId,
    UploadedFileData uploadedFileData
) implements Command {

}
