package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.values.files.UploadFileFormData;
import com.azat4dev.booking.shared.domain.values.user.UserId;

public record GeneratedUrlForUploadListingPhoto(
    UserId userId,
    ListingId listingId,
    UploadFileFormData formData
) implements DomainEventPayload, EventWithListingId {
}
