package com.azat4dev.booking.listingsms.commands.application.commands;

import com.azat4dev.booking.shared.application.events.ApplicationCommand;
import com.azat4dev.booking.shared.domain.values.user.UserId;

public record AddNewPhotoToListing(
    UserId userId,
    String operationId,
    String listingId,
    UploadedFileData uploadedFileData
) implements ApplicationCommand {

    public record UploadedFileData(
        String bucketName,
        String objectName
    ) {
    }
}

