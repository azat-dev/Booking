package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.queries.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.queries.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.queries.domain.values.RoomType;
import com.azat4dev.booking.listingsms.queries.domain.values.address.ListingAddress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public record ListingPrivateDetails(
    ListingId id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    OwnerId ownerId,
    ListingTitle title,
    ListingStatus status,
    Optional<ListingDescription> description,
    GuestsCapacity guestsCapacity,
    Optional<PropertyType> propertyType,
    Optional<RoomType> roomType,
    Optional<ListingAddress> address,
    List<ListingPhoto> photos
) {
}
