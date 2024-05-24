package com.azat4dev.bookingdemo.listingsms.commands.core.domain.handlers;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.bookingdemo.listingsms.commands.core.domain.commands.AddNewListing;
import com.azat4dev.bookingdemo.listingsms.commands.core.domain.entities.Listing;
import com.azat4dev.bookingdemo.listingsms.commands.core.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.bookingdemo.listingsms.commands.core.domain.values.ListingId;
import com.azat4dev.bookingdemo.listingsms.commands.core.domain.values.MakeNewListingId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddNewListingCommandHandler {

    private final ListingsRepository repository;
    private final MakeNewListingId makeListingId;

    public ListingId handle(AddNewListing command) throws DomainException {

        final var listingId = makeListingId.make();

        final var newListing = new Listing(
            listingId,
            command.getOwnerId(),
            command.getTitle()
        );

        repository.addNew(newListing);

        return listingId;
    }
}
