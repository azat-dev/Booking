package com.azat4dev.booking.shared.domain.interfaces.files;

import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;

import java.net.URL;
import java.time.ZonedDateTime;

public interface MediaObject {
    BucketName bucketName();
    MediaObjectName objectName();
    URL url();
    long size();
    ZonedDateTime lastModified();
    String contentType();
}
