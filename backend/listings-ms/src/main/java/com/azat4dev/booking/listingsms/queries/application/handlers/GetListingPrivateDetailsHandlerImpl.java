package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.PrivateListingDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.PrivateListings;
import com.azat4dev.booking.shared.application.ValidationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class GetListingPrivateDetailsHandlerImpl implements GetListingPrivateDetailsHandler {

    private final PrivateListings privateListings;

    @Override
    public PrivateListingDetails handle(GetListingPrivateDetails command) throws ListingNotFoundException {

        try {
            final var listingId = ListingId.checkAndMakeFrom(command.listingId());

            return privateListings.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException(listingId));

        } catch (ListingId.Exception.WrongFormat e) {
            throw ValidationException.withPath("listingId", e);
        }
    }
}
