package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

import java.time.LocalDateTime;

public record ListingPublished(
    ListingId listingId,
    LocalDateTime publishedAt
) implements DomainEventPayload {
}
