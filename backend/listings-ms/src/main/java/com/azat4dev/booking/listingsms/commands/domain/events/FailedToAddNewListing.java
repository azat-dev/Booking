package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import com.azat4dev.booking.shared.domain.event.DomainEventPayload;

public record FailedToAddNewListing(
    ListingId listingId,
    OwnerId ownerId,
    ListingTitle title
) implements DomainEventPayload {
}
