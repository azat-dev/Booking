package com.azat4dev.booking.searchlistingsms.common.acl.domain.events;

import com.azat4dev.booking.searchlistingsms.commands.domain.events.incoming.ListingPublished;
import com.azat4dev.booking.searchlistingsms.common.acl.domain.values.ListingInfo;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

public record ReceivedListingInfoForPublishedListing(
    ListingPublished inputEvent,
    ListingInfo listingInfo
) implements DomainEventPayload {
}
