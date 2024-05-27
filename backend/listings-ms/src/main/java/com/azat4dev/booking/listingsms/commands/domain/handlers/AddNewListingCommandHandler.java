package com.azat4dev.booking.listingsms.commands.domain.handlers;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingId;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.listingsms.commands.domain.commands.AddNewListing;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddNewListingCommandHandler {

    private final ListingsRepository repository;
    private final TimeProvider timeProvider;
    private final MakeNewListingId makeListingId;

    public ListingId handle(AddNewListing command) throws DomainException {

        final var listingId = makeListingId.make();
        final var now = timeProvider.currentTime();

        final var newListing = Listing.makeNewDraft(
            listingId,
            now,
            command.getOwnerId(),
            command.getTitle()
        );

        repository.addNew(newListing);

        return listingId;
    }
}
