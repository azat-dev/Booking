package com.azat4dev.booking.users.users_commands.domain.core.commands;

import com.azat4dev.booking.shared.domain.event.Command;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.Password;

public record ChangePassword(
    UserId userId,
    Password currentPassword,
    Password newPassword
) implements Command {
}
