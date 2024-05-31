package com.azat4dev.booking.users.users_commands.domain.core.events;

import com.azat4dev.booking.shared.domain.event.Command;
import com.azat4dev.booking.shared.domain.values.files.UploadedFileData;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;


public record FailedUpdateUserPhoto(
    IdempotentOperationId idempotentOperationId,
    UserId userId,
    UploadedFileData uploadedFileData
) implements Command {

}
