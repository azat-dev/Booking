package com.azat4dev.booking.shared.domain.values.files;


public record UploadedFileData(
    BucketName bucketName,
    MediaObjectName objectName
) {
}
