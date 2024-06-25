package com.azat4dev.booking.searchlistingsms.commands.domain.interfaces;

import com.azat4dev.booking.searchlistingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

import java.util.List;

public record ListingDetails(
    String title,
    String description,
    List<ListingPhoto> photos
) implements DomainEventPayload {

}
