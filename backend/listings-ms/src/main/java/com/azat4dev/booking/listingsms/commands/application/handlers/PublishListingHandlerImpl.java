package com.azat4dev.booking.listingsms.commands.application.handlers;

import com.azat4dev.booking.listingsms.commands.application.commands.PublishListing;
import com.azat4dev.booking.listingsms.commands.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.application.ValidationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class PublishListingHandlerImpl implements PublishListingHandler {

    private final Hosts hosts;
    private final ListingsCatalog listingsCatalog;

    @Override
    public void handle(PublishListing command) throws ValidationException, Exception.FailedToPublish, Exception.ListingNotFoundException {

        try {
            final var hostId = HostId.fromUserId(command.userId());
            final var listingId = ListingId.checkAndMakeFrom(command.listingId());

            final var listing = hosts.getById(hostId)
                .getListings()
                .findById(listingId)
                .orElseThrow(() -> new Exception.ListingNotFoundException(command.listingId()));

            listing.publish();
            listingsCatalog.update(listing);

        } catch (Listing.Exception.Publishing e) {
            throw new Exception.FailedToPublish();
        } catch (ListingId.Exception.WrongFormat e) {
            throw ValidationException.withPath("listingId", e);
        } catch (ListingsCatalog.Exception.ListingNotFound e) {
            throw new Exception.ListingNotFoundException(command.listingId());
        }
    }
}
