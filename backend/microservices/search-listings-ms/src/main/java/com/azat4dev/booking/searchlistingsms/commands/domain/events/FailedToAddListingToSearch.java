package com.azat4dev.booking.searchlistingsms.commands.domain.events;

import com.azat4dev.booking.searchlistingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

public record FailedToAddListingToSearch(
    ListingId listingId
) implements DomainEventPayload {
}
