package com.azat4dev.booking.searchlistingsms.common.acl.domain.values;

import com.azat4dev.booking.searchlistingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.searchlistingsms.common.domain.values.PropertyType;

public record ListingInfo(
    String listingTitle,
    String description,
    GuestsCapacity guestsCapacity,
    PropertyType propertyType
) {
}
