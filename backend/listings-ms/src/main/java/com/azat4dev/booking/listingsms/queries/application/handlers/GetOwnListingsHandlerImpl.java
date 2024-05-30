package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.queries.application.commands.GetOwnListings;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.Users;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public final class GetOwnListingsHandlerImpl implements GetOwnListingsHandler {

    private final Users users;

    @Override
    public List<ListingPrivateDetails> handle(GetOwnListings command) throws Exception.Forbidden {

        if (command.userId() == null) {
            throw new Exception.Forbidden();
        }

        final var currentUser = users.getById(command.userId());
        return currentUser.getListings()
            .listAll();
    }
}
