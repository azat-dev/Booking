package com.azat4dev.booking.listingsms.presentation.api.dto;

public record RequestAddListing(
    String operationId,
    String title,
    String description
) {
}
