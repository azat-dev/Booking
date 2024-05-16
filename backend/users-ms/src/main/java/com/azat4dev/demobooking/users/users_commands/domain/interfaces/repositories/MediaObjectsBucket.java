package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.FileExtension;

import java.net.URL;
import java.time.LocalDateTime;

public interface MediaObjectsBucket {

    URL generateUploadUrl(
        MediaObjectName objectName,
        FileExtension fileExtension,
        LocalDateTime expiresAt
    );

    void put(FileKey key, byte[] file);
}
