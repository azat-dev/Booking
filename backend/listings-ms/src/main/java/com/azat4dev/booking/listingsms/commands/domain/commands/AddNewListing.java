package com.azat4dev.booking.listingsms.commands.domain.commands;

public record AddNewListing(
    String operationId,
    String hostId,
    String title
) {
}
