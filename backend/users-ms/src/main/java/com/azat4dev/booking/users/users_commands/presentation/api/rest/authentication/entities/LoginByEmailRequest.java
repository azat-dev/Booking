package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.Password;

public record LoginByEmailRequest(String email, String password) {

    public EmailAddress parseEmail() throws ValidationException {
        try {
            return EmailAddress.checkAndMakeFromString(email);
        } catch (EmailAddress.ValidationException e) {
            throw ValidationException.withPath("email", e);
        }
    }

    public Password parsePassword() throws ValidationException {
        try {
            return Password.checkAndMakeFromString(password);
        } catch (Password.Exception e) {
            throw ValidationException.withPath("password", e);
        }
    }
}
