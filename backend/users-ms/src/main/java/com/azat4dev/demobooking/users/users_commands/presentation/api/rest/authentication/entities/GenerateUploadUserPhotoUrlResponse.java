package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

import java.util.Map;

public record GenerateUploadUserPhotoUrlResponse(
    String url,
    String objectName,
    Map<String,String> formData
) {
}
