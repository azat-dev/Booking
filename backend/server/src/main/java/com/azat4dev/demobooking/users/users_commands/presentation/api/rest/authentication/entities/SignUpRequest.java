package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.common.presentation.ControllerException;
import com.azat4dev.demobooking.common.presentation.ValidationException;
import com.azat4dev.demobooking.common.utils.Assert;
import com.azat4dev.demobooking.users.users_commands.domain.entities.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.entities.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.entities.LastName;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.values.Password;

public record SignUpRequest(
    FullNameDTO fullName,
    String email,
    String password
) {

    public FullName parseFullName() throws ControllerException {
        Assert.notNull(fullName, () -> ValidationException.with("NotNull", "fullName", "Full name can't be null"));
        try {
            return fullName.toDomain();
        } catch (FullName.ValidationException e) {
            throw ValidationException.withPath("fullName", e);
        } catch (FirstName.ValidationException e) {
            throw ValidationException.withPath("fullName.firstName", e);
        } catch (LastName.ValidationException e) {
            throw ValidationException.withPath("fullName.lastName", e);
        }
    }

    public EmailAddress parseEmail() throws ValidationException {
        try {
            return EmailAddress.makeFromString(email);
        } catch (DomainException e) {
            throw ValidationException.withPath("email", e);
        }
    }

    public Password parsePassword() throws ValidationException {

        try {
            return Password.makeFromString(password);
        } catch (DomainException e) {
            throw ValidationException.withPath("password", e);
        }
    }
}