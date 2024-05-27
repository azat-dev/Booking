package com.azat4dev.booking.listingsms.presentation.api.dto;

public record AddListingRequest(
    String operationId,
    String title,
    String description
) {
}
