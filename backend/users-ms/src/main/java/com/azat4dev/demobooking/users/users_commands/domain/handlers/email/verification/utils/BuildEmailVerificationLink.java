package com.azat4dev.demobooking.users.users_commands.domain.handlers.email.verification.utils;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;

@FunctionalInterface
public interface BuildEmailVerificationLink {
    String execute(EmailVerificationToken token);
}
