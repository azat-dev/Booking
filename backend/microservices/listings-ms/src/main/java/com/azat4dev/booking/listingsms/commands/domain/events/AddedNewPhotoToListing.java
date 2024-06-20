package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

public record AddedNewPhotoToListing(
    ListingId listingId,
    ListingPhoto photo
) implements DomainEventPayload {
}
