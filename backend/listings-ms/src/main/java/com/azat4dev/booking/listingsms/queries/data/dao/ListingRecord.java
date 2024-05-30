package com.azat4dev.booking.listingsms.queries.data.dao;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public record ListingRecord(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    UUID ownerId,
    String title,
    String status,
    Optional<String> description
) {
}
