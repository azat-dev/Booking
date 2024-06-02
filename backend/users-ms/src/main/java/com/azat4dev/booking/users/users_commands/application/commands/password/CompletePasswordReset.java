package com.azat4dev.booking.users.users_commands.application.commands.password;

import com.azat4dev.booking.shared.application.events.ApplicationCommand;

public record CompletePasswordReset(
    String operationId,
    String newPassword,
    String passwordResetToken
) implements ApplicationCommand {
}
