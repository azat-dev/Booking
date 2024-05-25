package com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.utils;

import com.azat4dev.booking.users.users_commands.domain.core.entities.User;

public interface VerificationEmailBuilder {
    VerificationEmailBuilderResult build(User user);
}
