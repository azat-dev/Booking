package com.azat4dev.booking.listingsms.commands.data.dao.listings;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public record ListingData(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    UUID ownerId,
    String title,
    String status,
    Optional<String> description
    ) {
}
