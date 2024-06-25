package com.azat4dev.booking.searchlistingsms.commands.domain.values;

import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
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

    public static ListingPhoto makeWithoutChecks(String id, BucketName bucketName, MediaObjectName objectName) {
        return new ListingPhoto(id, bucketName, objectName);
    }
}
