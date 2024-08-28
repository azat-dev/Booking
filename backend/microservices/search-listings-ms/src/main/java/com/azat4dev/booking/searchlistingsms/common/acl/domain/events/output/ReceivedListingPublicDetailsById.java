package com.azat4dev.booking.searchlistingsms.common.acl.domain.events.output;

import com.azat4dev.booking.searchlistingsms.common.domain.values.ListingId;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

public record ReceivedListingPublicDetailsById(
    ListingId listingId,
    GetListingPublicDetailsById publicDetails
) implements DomainEventPayload {
}
