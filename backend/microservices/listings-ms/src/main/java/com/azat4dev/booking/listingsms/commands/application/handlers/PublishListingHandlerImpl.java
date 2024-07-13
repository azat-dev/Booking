package com.azat4dev.booking.listingsms.commands.application.handlers;

import com.azat4dev.booking.listingsms.commands.application.commands.PublishListing;
import com.azat4dev.booking.listingsms.commands.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.application.ValidationException;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Observed
@AllArgsConstructor
public class PublishListingHandlerImpl implements PublishListingHandler {

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

            log.atInfo()
                .addKeyValue("userId", command::userId)
                .addKeyValue("listingId", command::listingId)
                .log("Listing published");

        } catch (Listing.Exception.Publishing e) {

            log.atError()
                .addKeyValue("userId", command::userId)
                .addKeyValue("listingId", command::listingId)
                .addKeyValue("errorMessage", e::getMessage)
                .log("Failed to publish listing");

            throw new Exception.FailedToPublish();
        } catch (ListingId.Exception.WrongFormat e) {

            log.atWarn()
                .addKeyValue("listingId", command::listingId)
                .addKeyValue("errorMessage", e::getMessage)
                .log("Wrong listing ID format");

            throw ValidationException.withPath("listingId", e);
        } catch (ListingsCatalog.Exception.ListingNotFound e) {

            log.atWarn()
                .addKeyValue("listingId", command::listingId)
                .addKeyValue("errorMessage", e::getMessage)
                .log("Listing not found");
            throw new Exception.ListingNotFoundException(command.listingId());
        }
    }
}
