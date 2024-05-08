package com.azat4dev.demobooking.users.users_commands.domain.services;

import com.azat4dev.demobooking.users.users_commands.domain.values.UserId;

public interface VerificationEmailBuilder {
    VerificationEmailBuilderResult build(UserId userId);
}
