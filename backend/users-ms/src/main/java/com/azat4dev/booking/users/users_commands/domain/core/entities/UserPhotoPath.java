package com.azat4dev.booking.users.users_commands.domain.core.entities;

import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectName;


public record UserPhotoPath(
    BucketName bucketName,
    MediaObjectName objectName
) {
}
