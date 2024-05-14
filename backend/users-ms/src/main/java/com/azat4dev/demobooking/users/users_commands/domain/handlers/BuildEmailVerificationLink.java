package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;

@FunctionalInterface
public interface BuildEmailVerificationLink {
    String execute(EmailVerificationToken token);
}
