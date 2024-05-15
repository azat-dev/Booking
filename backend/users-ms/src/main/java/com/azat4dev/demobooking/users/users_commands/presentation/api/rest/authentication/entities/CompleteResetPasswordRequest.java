package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

public record CompleteResetPasswordRequest(
    String idempotencyKey,
    String newPassword,
    String resetPasswordToken
) {
}
