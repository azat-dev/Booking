package com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.utils;

import com.azat4dev.booking.shared.domain.core.UserId;

public interface VerificationEmailBuilder {
    VerificationEmailBuilderResult build(UserId userId);
}
