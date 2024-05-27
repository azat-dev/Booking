package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.events.NewListingAdded;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListingsCatalogImpl implements ListingsCatalog {

    private final UnitOfWorkFactory unitOfWorkFactory;
    private final TimeProvider timeProvider;

    @Override
    public void addNew(
        ListingId listingId,
        OwnerId ownerId,
        ListingTitle title
    ) {
        final var now = timeProvider.currentTime();

        final var newListing = Listing.makeNewDraft(
            listingId,
            now,
            ownerId,
            title
        );

        final var unitOfWork = unitOfWorkFactory.make();

        try {
            final var listings = unitOfWork.getListingsRepository();
            final var outbox = unitOfWork.getOutboxEventsRepository();

            listings.addNew(newListing);

            outbox.publish(
                new NewListingAdded(
                    listingId,
                    ownerId,
                    title
                )
            );

            unitOfWork.save();
        } catch (Throwable e) {
            unitOfWork.rollback();
            throw e;
        }
    }
}