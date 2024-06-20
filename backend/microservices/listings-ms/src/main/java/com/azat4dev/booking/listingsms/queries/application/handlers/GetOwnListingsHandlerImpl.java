package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.queries.application.commands.GetOwnListings;
import com.azat4dev.booking.listingsms.queries.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public final class GetOwnListingsHandlerImpl implements GetOwnListingsHandler {

    private final Hosts hosts;

    @Override
    public List<ListingPrivateDetails> handle(GetOwnListings command) throws Exception.Forbidden {

        if (command.hostId() == null) {
            throw new Exception.Forbidden();
        }

        final var currentUser = hosts.getById(command.hostId());
        return currentUser.getListings()
            .listAll();
    }
}
