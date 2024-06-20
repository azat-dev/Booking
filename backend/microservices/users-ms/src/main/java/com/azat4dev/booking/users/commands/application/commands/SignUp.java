package com.azat4dev.booking.users.commands.application.commands;

import com.azat4dev.booking.shared.application.events.ApplicationCommand;

public record SignUp(
    FullName fullName,
    String email,
    String password
) implements ApplicationCommand {

    public record FullName (
        String firstName,
        String lastName
    ) {
    }
}
