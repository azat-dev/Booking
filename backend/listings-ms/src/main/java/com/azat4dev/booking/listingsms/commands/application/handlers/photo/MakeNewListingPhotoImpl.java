package com.azat4dev.booking.listingsms.commands.application.handlers.photo;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.UploadedFileData;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public final class MakeNewListingPhotoImpl implements MakeNewListingPhoto {

    private final BucketName bucketName;
    private final MediaObjectsBucket bucket;

    @Override
    public ListingPhoto execute(UploadedFileData uploadedFileData) {

        final var mediaObject = bucket.getObject(uploadedFileData.objectName());

        return new ListingPhoto(
            UUID.randomUUID().toString(),
            bucketName,
            mediaObject.objectName()
        );
    }
}
