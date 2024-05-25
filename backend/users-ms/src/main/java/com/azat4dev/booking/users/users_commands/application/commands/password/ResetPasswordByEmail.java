package com.azat4dev.booking.users.users_commands.application.commands.password;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class ResetPasswordByEmail {

    private final String operationId;
    private final String email;
}
