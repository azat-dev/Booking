package com.azat4dev.booking.searchlistingsms.commands.domain.events.incoming;

import com.azat4dev.booking.searchlistingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.searchlistingsms.common.acl.domain.values.ListingInfo;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

import java.time.LocalDateTime;

public record ListingPublished(
    ListingId listingId,
    LocalDateTime publishedAt,
    ListingInfo listingInfo
) implements DomainEventPayload {
}
