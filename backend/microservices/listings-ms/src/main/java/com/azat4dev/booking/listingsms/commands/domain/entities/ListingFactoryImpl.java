package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.application.handlers.photo.MakeNewListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ListingFactoryImpl implements ListingFactory {

    public final MakeNewListingPhoto makeNewListingPhoto;

    @Override
    public Listing internalMake(
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
    ) {
        return ListingImpl.internalMake(
            id,
            status,
            createdAt,
            updatedAt,
            hostId,
            title,
            description,
            propertyType,
            roomType,
            address,
            guestsCapacity,
            photos,
            makeNewListingPhoto
        );
    }

    @Override
    public Listing makeNewDraft(
        ListingId id,
        LocalDateTime createdAt,
        HostId hostId,
        ListingTitle title
    ) {

        return ListingImpl.internalMake(
            id,
            ListingStatus.DRAFT,
            createdAt,
            createdAt,
            hostId,
            title,
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            GuestsCapacity.DEFAULT,
            List.of(),
            makeNewListingPhoto
        );
    }
}
