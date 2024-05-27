package com.azat4dev.booking.listingsms.commands.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.commands.AddNewListing;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.application.ValidationException;

public interface AddNewListingHandler {
    ListingId handle(AddNewListing command) throws ValidationException;
}
