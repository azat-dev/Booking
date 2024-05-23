package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities;

public record UpdateUserPhotoRequestBody(
    String idempotentOperationId,
    UploadedFileDataDTO uploadedFile
) {

}
