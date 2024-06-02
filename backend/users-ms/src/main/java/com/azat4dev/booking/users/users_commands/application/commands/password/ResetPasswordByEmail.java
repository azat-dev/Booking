package com.azat4dev.booking.users.users_commands.application.commands.password;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.azat4dev.booking.shared.application.events.ApplicationCommand;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class ResetPasswordByEmail implements ApplicationCommand {

    private final String operationId;
    private final String email;
}
