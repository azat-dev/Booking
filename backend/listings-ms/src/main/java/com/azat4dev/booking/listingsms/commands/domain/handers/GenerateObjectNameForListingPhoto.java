package com.azat4dev.booking.listingsms.commands.domain.handers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;


public interface GenerateObjectNameForListingPhoto {
    MediaObjectName execute(UserId userId, ListingId listingId, PhotoFileExtension extension);
}
