package com.azat4dev.booking.users.users_commands.application.commands;

public record SignUp(
    FullName fullName,
    String email,
    String password
) {

    public static record FullName (
        String firstName,
        String lastName
    ) {
    }
}
