package com.azat4dev.booking.users.commands.application.commands.email.verification;

import com.azat4dev.booking.shared.application.events.ApplicationCommand;

public record CompleteEmailVerification(String token) implements ApplicationCommand {
}
