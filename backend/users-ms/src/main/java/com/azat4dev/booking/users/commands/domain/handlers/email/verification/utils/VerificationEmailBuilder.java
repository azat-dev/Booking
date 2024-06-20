package com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils;

import com.azat4dev.booking.users.commands.domain.core.entities.User;

public interface VerificationEmailBuilder {
    VerificationEmailBuilderResult build(User user);
}
