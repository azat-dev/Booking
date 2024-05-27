package com.azat4dev.booking.listingsms.commands.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.commands.AddNewListing;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import com.azat4dev.booking.shared.application.ValidationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class AddNewListingHandlerImpl implements AddNewListingHandler {

    private final MakeNewListingId makeListingId;
    private final ListingsCatalog listings;

    public ListingId handle(AddNewListing command) throws ValidationException {

        final var listingId = makeListingId.make();

        try {
            listings.addNew(
                listingId,
                OwnerId.checkAndMakeFrom(command.ownerId()),
                ListingTitle.checkAndMakeFrom(command.title())
            );
        } catch (ListingTitle.Exception.WrongFormat | ListingTitle.Exception.MaxLength e) {
            throw ValidationException.withPath("title", e);
        }

        return listingId;
    }
}
