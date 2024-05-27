package com.azat4dev.booking.listingsms.queries.presentation.api.entities;

public record GetListingPrivateDetailsResponse(
    String listingId,
    String title,
    String description,
    String status
) {
}
