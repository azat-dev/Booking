package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.Users;
import com.azat4dev.booking.shared.application.ValidationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class GetListingPrivateDetailsHandlerImpl implements GetListingPrivateDetailsHandler {

    private final Users users;

    @Override
    public ListingPrivateDetails handle(GetListingPrivateDetails command) throws ListingNotFoundException {

        try {
            final var listingId = ListingId.checkAndMakeFrom(command.listingId());
            final var currentUser = users.getById(command.userId());

            return currentUser
                .getListings()
                .findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException(listingId));

        } catch (ListingId.Exception.WrongFormat e) {
            throw ValidationException.withPath("listingId", e);
        }
    }
}
