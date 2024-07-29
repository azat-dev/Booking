package com.azat4dev.booking.listingsms.commands.application.handlers.photo;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;

@FunctionalInterface
public interface MakeNewListingPhoto {

    ListingPhoto execute(BucketName bucketName, MediaObjectName objectName);
}
