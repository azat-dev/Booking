package com.azat4dev.booking.shared.domain.values.files;

import java.net.URL;
import java.util.Map;

public record UploadFileFormData(
    URL url,
    BucketName bucketName,
    MediaObjectName objectName,
    Map<String, String> value
) {
}
