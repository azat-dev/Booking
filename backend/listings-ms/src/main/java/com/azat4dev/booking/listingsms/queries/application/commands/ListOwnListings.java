package com.azat4dev.booking.listingsms.queries.application.commands;

import com.azat4dev.booking.shared.domain.core.UserId;

public record ListOwnListings(
    UserId userId
) {
}
