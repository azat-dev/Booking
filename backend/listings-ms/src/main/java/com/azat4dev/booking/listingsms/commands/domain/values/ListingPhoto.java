package com.azat4dev.booking.listingsms.commands.domain.values;

import com.azat4dev.booking.shared.domain.values.BucketName;
import com.azat4dev.booking.shared.domain.values.MediaObjectName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public final class ListingPhoto {

    private final String id;
    private final BucketName bucketName;
    private final MediaObjectName objectName;
}
