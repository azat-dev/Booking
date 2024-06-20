package com.azat4dev.booking.users.commands.domain.core.entities;


import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;

public record UserPhotoPath(
    BucketName bucketName,
    MediaObjectName objectName
) {
}
