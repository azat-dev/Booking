package com.azat4dev.booking.listingsms.queries.application.commands;

import com.azat4dev.booking.shared.domain.values.user.UserId;

public record GetListingPrivateDetails(
    UserId userId,
    String listingId
) {
}
