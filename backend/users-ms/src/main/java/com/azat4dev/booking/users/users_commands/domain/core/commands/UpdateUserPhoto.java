package com.azat4dev.booking.users.users_commands.domain.core.commands;

import com.azat4dev.booking.shared.domain.event.Command;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.files.UploadedFileData;


public record UpdateUserPhoto(
    IdempotentOperationId idempotentOperationId,
    UserId userId,
    UploadedFileData uploadedFileData
) implements Command {

}
