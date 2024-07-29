package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ListingFactory {

    Listing makeNewDraft(
        ListingId id,
        LocalDateTime createdAt,
        HostId hostId,
        ListingTitle title
    );

    Listing internalMake(
        ListingId id,
        ListingStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        HostId hostId,
        ListingTitle title,
        Optional<ListingDescription> description,
        Optional<PropertyType> propertyType,
        Optional<RoomType> roomType,
        Optional<ListingAddress> address,
        GuestsCapacity guestsCapacity,
        List<ListingPhoto> photos
    );
}
