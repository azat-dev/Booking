package com.azat4dev.booking.shared.infrastructure.api;

import com.azat4dev.booking.shared.domain.values.user.UserId;

import java.util.Optional;

@FunctionalInterface
public interface CurrentAuthenticatedUserIdProvider {

    Optional<UserId> get();
}
