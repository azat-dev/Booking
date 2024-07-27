package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;

import java.time.LocalDateTime;
import java.util.List;

public record ListingPublicDetails(
    ListingId id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    HostId hostId,
    ListingTitle title,
    ListingStatus status,
    ListingDescription description,
    GuestsCapacity guestsCapacity,
    PropertyType propertyType,
    RoomType roomType,
    ListingAddress address,
    List<ListingPhoto> photos
) {
}
