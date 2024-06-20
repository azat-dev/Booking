package com.azat4dev.booking.shared.domain.interfaces.files;

import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.UploadFileFormData;

import java.net.URL;
import java.time.Duration;
import java.util.Optional;

public interface MediaObjectsBucket {


    MediaObject getObject(MediaObjectName objectName);

    URL generateUploadUrl(
        MediaObjectName objectName,
        Duration expiresIn
    );

    UploadFileFormData generateUploadFormData(
        MediaObjectName objectName,
        Duration expiresIn,
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
