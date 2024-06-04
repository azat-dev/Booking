package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

import java.time.LocalDateTime;
import java.util.Optional;

public record ListingUpdated(
    ListingId listingId,
    LocalDateTime updatedAt,
    State previousState,
    State newState
) implements DomainEventPayload {

    public record State(
        ListingStatus status,
        ListingTitle title,
        Optional<ListingDescription> description,
        Optional<PropertyType> propertyType,
        GuestsCapacity guestsCapacity,
        Optional<ListingAddress> address
    ) {
    }
}
