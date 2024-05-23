package com.azat4dev.booking.users.users_commands.domain.core.values.files;

import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectName;

public record UploadedFileData(
    BucketName bucketName,
    MediaObjectName objectName
) {
}
