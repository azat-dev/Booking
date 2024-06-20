package com.azat4dev.booking.users.commands.domain.core.commands;

import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.user.FullName;

public record SendVerificationEmail(
    UserId userId,
    EmailAddress email,
    FullName fullName,
    int attempt
) implements Command {

}
