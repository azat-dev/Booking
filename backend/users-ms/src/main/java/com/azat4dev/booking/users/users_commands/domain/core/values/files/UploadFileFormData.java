package com.azat4dev.booking.users.users_commands.domain.core.values.files;

import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.BucketName;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectName;

import java.net.URL;
import java.util.Map;

public record UploadFileFormData(
    URL url,
    BucketName bucketName,
    MediaObjectName objectName,
    Map<String, String> value
) {
}
