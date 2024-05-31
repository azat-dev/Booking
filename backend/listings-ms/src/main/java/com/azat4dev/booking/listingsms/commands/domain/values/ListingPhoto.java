package com.azat4dev.booking.listingsms.commands.domain.values;

import com.azat4dev.booking.shared.domain.values.BucketName;
import com.azat4dev.booking.shared.domain.values.MediaObjectName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public final class ListingPhoto {

    private final String id;
    private final BucketName bucketName;
    private final MediaObjectName objectName;

    public static ListingPhoto dangerouslyMakeFrom(String id, BucketName bucketName, MediaObjectName objectName) {
        return new ListingPhoto(id, bucketName, objectName);
    }
}
