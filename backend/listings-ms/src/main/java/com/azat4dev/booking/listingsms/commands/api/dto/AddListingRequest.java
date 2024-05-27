package com.azat4dev.booking.listingsms.commands.api.dto;

public record AddListingRequest(
    String operationId,
    String title,
    String description
) {
}
