package com.azat4dev.booking.listingsms.queries.data.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record ListingRecord(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    UUID hostId,
    String title,
    String status,
    Optional<String> description,
    GuestsCapacity guestsCapacity,
    Optional<String> propertyType,
    Optional<String> roomType,
    Optional<Address> address,
    List<Photo> photos
) {

    public record GuestsCapacity(
        int adults,
        int children,
        int infants
    ) {
    }

    public record Address(
        String country,
        String city,
        String street
    ){}

    public record Photo(
        String id,
        String bucketName,
        String objectName
    ){}
}
