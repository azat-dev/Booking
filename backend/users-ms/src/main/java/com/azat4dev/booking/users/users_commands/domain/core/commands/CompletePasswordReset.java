package com.azat4dev.booking.users.users_commands.domain.core.commands;

import com.azat4dev.booking.shared.domain.event.Command;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;

public record CompletePasswordReset(
    IdempotentOperationId idempotentOperationId,
    EncodedPassword newPassword,
    TokenForPasswordReset passwordResetToken
) implements Command {
}
