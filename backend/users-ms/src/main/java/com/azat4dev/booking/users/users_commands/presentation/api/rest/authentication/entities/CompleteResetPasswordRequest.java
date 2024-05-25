package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities;

public record CompleteResetPasswordRequest(
    String operationId,
    String newPassword,
    String resetPasswordToken
) {

}
