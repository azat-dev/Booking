package com.azat4dev.booking.searchlistingsms.common.domain.events.incoming;

import com.azat4dev.booking.searchlistingsms.common.domain.values.*;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

import java.util.List;

public record NewListingPublished(
    ListingId listingId,
    String title,
    String description,
    GuestsCapacity guestsCapacity,
    ListingType listingType,
    PropertyType propertyType,
    RoomType roomType,
    List<ListingPhoto> photos
) implements DomainEventPayload {
}
