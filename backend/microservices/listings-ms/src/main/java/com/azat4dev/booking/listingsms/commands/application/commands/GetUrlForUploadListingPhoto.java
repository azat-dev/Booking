package com.azat4dev.booking.listingsms.commands.application.commands;

import com.azat4dev.booking.shared.application.events.ApplicationCommand;

public record GetUrlForUploadListingPhoto(
    String operationId,
    String userId,
    String listingId,
    String fileExtension,
    int fileSize
) implements ApplicationCommand {
}

