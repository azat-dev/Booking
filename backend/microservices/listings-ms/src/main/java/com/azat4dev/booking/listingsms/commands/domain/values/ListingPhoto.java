package com.azat4dev.booking.listingsms.commands.domain.values;

import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public final class ListingPhoto {

    private final ListingPhotoId id;
    private final BucketName bucketName;
    private final MediaObjectName objectName;

    public static ListingPhoto makeWithoutChecks(
        ListingPhotoId id,
        BucketName bucketName,
        MediaObjectName objectName
    ) {
        return new ListingPhoto(id, bucketName, objectName);
    }

    public static ListingPhoto makeWithChecks(
        ListingPhotoId id,
        BucketName bucketName,
        MediaObjectName objectName
    ) {
        return new ListingPhoto(id, bucketName, objectName);
    }

    @Override
    public String toString() {
        return "ListingPhoto{" +
            "id=" + id +
            ", bucketName=" + bucketName +
            ", objectName=" + objectName +
            '}';
    }
}
