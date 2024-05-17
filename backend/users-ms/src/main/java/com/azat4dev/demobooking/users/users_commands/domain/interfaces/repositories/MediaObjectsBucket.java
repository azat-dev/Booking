package com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.UploadFileFormData;

import java.net.URL;
import java.util.Optional;

public interface MediaObjectsBucket {

    URL generateUploadUrl(
        MediaObjectName objectName,
        int expiresInSeconds
    );


    UploadFileFormData generateUploadFormData(
        MediaObjectName objectName,
        int expiresInSeconds,
        Policy policy
    );

    void put(FileKey key, byte[] file);

    enum ConditionType {
        EQUALS,
        STARTS_WITH
    }

    record FileSizeRange(
        int min,
        int max
    ) {
    }

    record Condition(
        String key,
        ConditionType type,
        String value
    ) {
    }

    record Policy(
        Optional<FileSizeRange> fileSizeRange,
        Condition[] conditions
    ) {

    }
}
