package com.azat4dev.booking.shared.domain.interfaces.files;

import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.UploadFileFormData;

import java.net.URL;
import java.util.Optional;

public interface MediaObjectsBucket {


    MediaObject getObject(MediaObjectName objectName);

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
