package com.azat4dev.booking.listingsms.queries.application.commands;

import com.azat4dev.booking.shared.application.events.ApplicationCommand;
import com.azat4dev.booking.shared.domain.values.user.UserId;

public record GetListingPrivateDetails(
    UserId hostId,
    String listingId
) implements ApplicationCommand {
}
