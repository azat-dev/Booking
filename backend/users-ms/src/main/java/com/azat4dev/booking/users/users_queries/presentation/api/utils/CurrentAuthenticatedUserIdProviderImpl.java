package com.azat4dev.booking.users.users_queries.presentation.api.utils;

import com.azat4dev.booking.shared.domain.core.UserId;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class CurrentAuthenticatedUserIdProviderImpl implements CurrentAuthenticatedUserIdProvider {

    @Override
    public Optional<UserId> get() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }

        try {
            final var userId = UserId.checkAndMakeFrom(authentication.getName());
            return Optional.of(userId);

        } catch (UserId.WrongFormatException e) {
            return Optional.empty();
        }
    }
}
