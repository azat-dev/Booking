package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.commands.infrastructure.persistence.dao.listings.ListingPhotoData;

@FunctionalInterface
public interface MapListingPhoto {

    ListingPhoto map(ListingPhotoData data);
}
