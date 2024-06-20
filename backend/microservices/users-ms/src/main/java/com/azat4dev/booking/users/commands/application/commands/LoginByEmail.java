package com.azat4dev.booking.users.commands.application.commands;

import com.azat4dev.booking.shared.application.events.ApplicationCommand;

public record LoginByEmail(
    String email,
    String password
) implements ApplicationCommand {
}
