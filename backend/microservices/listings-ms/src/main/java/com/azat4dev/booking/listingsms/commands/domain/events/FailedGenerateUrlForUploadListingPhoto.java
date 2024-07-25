package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;

public record FailedGenerateUrlForUploadListingPhoto(
    IdempotentOperationId operationId,
    UserId userId,
    ListingId listingId,
    PhotoFileExtension fileExtension,
    int fileSize
) implements DomainEventPayload, EventWithListingId {
}