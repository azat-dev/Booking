package com.azat4dev.booking.users.users_queries.presentation.api.utils;

import com.azat4dev.booking.shared.domain.core.UserId;

import java.util.Optional;

@FunctionalInterface
public interface CurrentAuthenticatedUserIdProvider {

    Optional<UserId> get();
}
