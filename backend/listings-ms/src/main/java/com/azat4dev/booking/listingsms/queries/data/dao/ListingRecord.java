package com.azat4dev.booking.listingsms.queries.data.dao;

import java.util.Optional;
import java.util.UUID;

public record ListingRecord(
    UUID id,
    String title,
    Optional<String> description
) {
}
