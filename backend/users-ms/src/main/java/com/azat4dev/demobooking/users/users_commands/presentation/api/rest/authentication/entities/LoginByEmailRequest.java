package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.Password;
import com.azat4dev.demobooking.common.presentation.ValidationException;

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
            return Password.makeFromString(password);
        } catch (Password.ValidationException e) {
            throw ValidationException.withPath("password", e);
        }
    }
}
