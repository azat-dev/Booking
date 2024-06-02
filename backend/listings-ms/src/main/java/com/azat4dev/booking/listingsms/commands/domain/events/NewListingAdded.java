package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

public record NewListingAdded(
    ListingId listingId,
    OwnerId ownerId,
    ListingTitle title
) implements DomainEventPayload {
}
