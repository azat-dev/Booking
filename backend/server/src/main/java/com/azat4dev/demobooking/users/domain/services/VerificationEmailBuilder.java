package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.users.domain.values.UserId;

public interface VerificationEmailBuilder {
    VerificationEmailBuilderResult build(UserId userId);
}
