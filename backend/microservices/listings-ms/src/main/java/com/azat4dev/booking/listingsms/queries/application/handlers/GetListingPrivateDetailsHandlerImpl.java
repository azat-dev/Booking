package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
import com.azat4dev.booking.shared.application.ValidationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class GetListingPrivateDetailsHandlerImpl implements GetListingPrivateDetailsHandler {

    private final Hosts hosts;

    @Override
    public ListingPrivateDetails handle(GetListingPrivateDetails command) throws ListingNotFoundException {

        try {
            final var listingId = ListingId.checkAndMakeFrom(command.listingId());
            final var host = hosts.getById(HostId.fromUserId(command.hostId()));

            return host
                .getListings()
                .findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException(listingId));

        } catch (ListingId.Exception.WrongFormat e) {
            throw ValidationException.withPath("listingId", e);
        }
    }
}
