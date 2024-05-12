package com.azat4dev.demobooking.users.users_commands.domain.core.commands;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

public record CreateUser(
    UserId userId,
    FullName fullName,
    EmailAddress email,
    EncodedPassword encodedPassword
) {
}
