package com.azat4dev.booking.users.users_commands.domain.core.commands;

import com.azat4dev.booking.shared.domain.event.Command;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.FullName;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;

public record SendVerificationEmail(
    UserId userId,
    EmailAddress email,
    FullName fullName,
    int attempt
) implements Command {

}
