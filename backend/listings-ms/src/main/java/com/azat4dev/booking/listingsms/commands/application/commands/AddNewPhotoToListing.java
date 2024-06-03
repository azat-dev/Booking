package com.azat4dev.booking.listingsms.commands.application.commands;

import com.azat4dev.booking.shared.application.events.ApplicationCommand;

public record AddNewPhotoToListing(
    String operationId,
    String userId,
    String listingId,
    UploadedFileData uploadedFileData
) implements ApplicationCommand {

    public record UploadedFileData(
        String bucketName,
        String objectName
    ) {
    }
}

