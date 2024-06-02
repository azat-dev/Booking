package com.azat4dev.booking.listingsms.commands.application.commands;

public record GetUrlForUploadListingPhoto(
    String operationId,
    String userId,
    String listingId,
    String fileExtension,
    int fileSize
) {
}

