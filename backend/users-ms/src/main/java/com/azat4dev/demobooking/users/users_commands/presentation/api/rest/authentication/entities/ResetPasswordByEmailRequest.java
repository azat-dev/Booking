package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

public record ResetPasswordByEmailRequest(
    String idempotentOperationId,
    String email
) {
}
