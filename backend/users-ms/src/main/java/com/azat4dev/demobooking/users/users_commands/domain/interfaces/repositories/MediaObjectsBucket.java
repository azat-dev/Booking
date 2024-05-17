package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

import java.net.URL;

public interface MediaObjectsBucket {

    URL generateUploadUrl(
        MediaObjectName objectName,
        int expiresInSeconds
    );

    void put(FileKey key, byte[] file);
}
