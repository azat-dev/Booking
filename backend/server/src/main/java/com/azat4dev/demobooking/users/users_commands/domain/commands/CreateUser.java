package com.azat4dev.demobooking.users.users_commands.domain.commands;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.users.users_commands.domain.entities.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.values.UserId;

public record CreateUser(
    CommandId id,
    UserId userId,
    FullName fullName,
    EmailAddress email,
    EncodedPassword encodedPassword
) {
}
