package com.azat4dev.booking.listingsms.commands.application.commands;

import com.azat4dev.booking.shared.domain.values.user.UserId;

public record PublishListing(UserId userId, String listingId) {
}
