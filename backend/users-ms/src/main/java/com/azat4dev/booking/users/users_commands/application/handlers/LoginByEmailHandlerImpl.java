package com.azat4dev.booking.users.users_commands.application.handlers;

import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.users.users_commands.application.commands.LoginByEmail;
import com.azat4dev.booking.users.users_commands.domain.core.entities.User;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.Password;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.PasswordService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class LoginByEmailHandlerImpl implements LoginByEmailHandler {

    private final PasswordService passwordService;
    private final Users users;

    @Override
    public User handle(LoginByEmail command) throws Exception.WrongCredentials, ValidationException {

        try {
            final var email = EmailAddress.checkAndMakeFromString(command.email());
            final var password = Password.checkAndMakeFromString(command.password());

            final var user = users.findByEmail(email)
                .orElseThrow(Exception.WrongCredentials::new);

            if (!passwordService.matches(password, user.getEncodedPassword())) {
                throw new Exception.WrongCredentials();
            }

            return user;
        } catch (Password.Exception e) {
            throw ValidationException.withPath("password", e);
        } catch (EmailAddress.WrongFormatException e) {
            throw ValidationException.withPath("email", e);
        }
    }
}
