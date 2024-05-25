package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.booking.users.users_commands.application.commands.password.ResetPasswordByEmail;

public final class ResetPasswordByEmailRequest extends ResetPasswordByEmail {
    public ResetPasswordByEmailRequest(String idempotentOperationToken, String email) {
        super(idempotentOperationToken, email);
    }
}
