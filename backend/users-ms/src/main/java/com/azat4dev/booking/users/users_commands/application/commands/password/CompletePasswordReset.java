package com.azat4dev.booking.users.users_commands.application.commands.password;

public record CompletePasswordReset(
    String operationId,
    String newPassword,
    String passwordResetToken
) {
}
