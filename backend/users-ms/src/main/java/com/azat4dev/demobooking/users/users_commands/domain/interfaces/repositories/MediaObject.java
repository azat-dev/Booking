package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

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
