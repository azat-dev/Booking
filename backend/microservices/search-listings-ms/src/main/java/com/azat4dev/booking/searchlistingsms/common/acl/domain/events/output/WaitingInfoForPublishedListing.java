package com.azat4dev.booking.searchlistingsms.common.acl.domain.events.output;

import com.azat4dev.booking.searchlistingsms.common.domain.values.ListingId;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

import java.time.LocalDateTime;

public record WaitingInfoForPublishedListing(
    ListingId listingId,
    LocalDateTime publishedAt
) implements DomainEventPayload {
}
