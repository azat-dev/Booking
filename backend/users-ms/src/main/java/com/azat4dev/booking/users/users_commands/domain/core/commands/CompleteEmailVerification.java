package com.azat4dev.booking.users.users_commands.domain.core.commands;

import com.azat4dev.booking.shared.domain.event.Command;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;


public record CompleteEmailVerification(EmailVerificationToken token) implements Command {
}