package com.azat4dev.booking.listingsms.commands.application.handlers.photo;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhotoId;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;

@Observed
@AllArgsConstructor
public class MakeNewListingPhotoImpl implements MakeNewListingPhoto {

    private final MediaObjectsBucket bucket;

    @Override
    public ListingPhoto execute(BucketName bucketName, MediaObjectName objectName) {

        final var mediaObject = bucket.getObject(objectName);
        return new ListingPhoto(ListingPhotoId.generateNew(), bucketName, mediaObject.objectName());
    }
}
