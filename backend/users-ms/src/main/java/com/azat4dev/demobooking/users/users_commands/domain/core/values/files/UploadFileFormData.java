package com.azat4dev.demobooking.users.users_commands.domain.core.values.files;

import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectName;

import java.net.URL;
import java.util.Map;

public record UploadFileFormData(
    URL url,
    MediaObjectName objectName,
    Map<String, String> value
) {
}
