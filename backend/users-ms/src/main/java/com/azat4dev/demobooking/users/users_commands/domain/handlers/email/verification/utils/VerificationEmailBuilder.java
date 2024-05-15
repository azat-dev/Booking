package com.azat4dev.demobooking.users.users_commands.domain.handlers.email.verification.utils;

import com.azat4dev.demobooking.users.common.domain.values.UserId;

public interface VerificationEmailBuilder {
    VerificationEmailBuilderResult build(UserId userId);
}
