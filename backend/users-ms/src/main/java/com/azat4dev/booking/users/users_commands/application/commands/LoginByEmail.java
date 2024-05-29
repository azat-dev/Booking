package com.azat4dev.booking.users.users_commands.application.commands;

public record LoginByEmail(
    String email,
    String password
) {
}
