package com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities;

import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.Password;
import com.azat4dev.demobooking.common.presentation.ValidationException;

public record LoginByEmailRequest(String email, String password) {

    public EmailAddress parseEmail() throws ValidationException {
        try {
            return EmailAddress.makeFromString(email);
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
