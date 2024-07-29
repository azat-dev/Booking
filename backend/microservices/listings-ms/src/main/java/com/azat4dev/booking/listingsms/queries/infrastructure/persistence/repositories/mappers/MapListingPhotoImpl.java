package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhotoId;
import com.azat4dev.booking.listingsms.commands.infrastructure.persistence.dao.listings.ListingPhotoData;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;

public final class MapListingPhotoImpl implements MapListingPhoto {
    @Override
    public ListingPhoto map(ListingPhotoData photo) {
        return ListingPhoto.makeWithoutChecks(
            ListingPhotoId.makeWithoutChecks(photo.id()),
            BucketName.makeWithoutChecks(photo.bucketName()),
            MediaObjectName.dangerouslyMake(photo.objectName())
        );
    }
}
