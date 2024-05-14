package com.azat4dev.demobooking.users.users_commands.domain.core.commands;

import com.azat4dev.demobooking.common.domain.event.Command;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.Password;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.PasswordResetToken;

public record CompletePasswordReset(
    String idempotentOperationToken,
    Password newPassword,
    PasswordResetToken passwordResetToken
) implements Command {
}
