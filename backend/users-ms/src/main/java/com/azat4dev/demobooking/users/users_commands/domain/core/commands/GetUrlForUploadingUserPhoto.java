package com.azat4dev.demobooking.users.users_commands.domain.core.commands;

import com.azat4dev.demobooking.common.domain.event.Command;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.IdempotentOperationId;

import java.time.LocalDateTime;

public record GetUrlForUploadingUserPhoto(
    UserId userId,
    IdempotentOperationId operationId,
    LocalDateTime requestedAt
) implements Command {
}
