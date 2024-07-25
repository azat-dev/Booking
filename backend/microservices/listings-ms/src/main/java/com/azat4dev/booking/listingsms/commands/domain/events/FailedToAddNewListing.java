package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

public record FailedToAddNewListing(
    ListingId listingId,
    HostId hostId,
    ListingTitle title
) implements DomainEventPayload, EventWithListingId {
}
