package com.azat4dev.booking.listingsms.commands.infrastructure.persistence.dao.listings;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;

public record ListingPhotoData(
    String id,
    String bucketName,
    String objectName
) {

    public static ListingPhotoData fromDomain(ListingPhoto dm) {
        return new ListingPhotoData(
            dm.getId(),
            dm.getBucketName().getValue(),
            dm.getObjectName().getValue()
        );
    }
}
