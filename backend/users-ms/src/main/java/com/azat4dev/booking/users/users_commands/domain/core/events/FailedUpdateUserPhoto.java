package com.azat4dev.booking.users.users_commands.domain.core.events;

import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.UploadedFileData;
import com.azat4dev.booking.shared.domain.values.user.UserId;


public record FailedUpdateUserPhoto(
    IdempotentOperationId operationId,
    UserId userId,
    UploadedFileData uploadedFileData
) implements Command {

}
