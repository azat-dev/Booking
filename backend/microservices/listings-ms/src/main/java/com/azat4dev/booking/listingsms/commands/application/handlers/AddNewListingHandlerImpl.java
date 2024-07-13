package com.azat4dev.booking.listingsms.commands.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.commands.AddNewListing;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingId;
import com.azat4dev.booking.shared.application.ValidationException;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Observed
@AllArgsConstructor
public class AddNewListingHandlerImpl implements AddNewListingHandler {

    private final MakeNewListingId makeListingId;
    private final ListingsCatalog listings;

    public ListingId handle(AddNewListing command) throws ValidationException {

        final var listingId = makeListingId.make();

        try {
            listings.addNew(
                listingId,
                HostId.checkAndMakeFrom(command.hostId()),
                ListingTitle.checkAndMakeFrom(command.title())
            );

            log.atInfo()
                .addKeyValue("hostId", command::hostId)
                .addKeyValue("listingId", listingId::getValue)
                .addKeyValue("title", command::title)
                .log("New listing added");

        } catch (ListingTitle.Exception.WrongFormat | ListingTitle.Exception.MaxLength e) {
            log.atWarn()
                .addKeyValue("title", command::title)
                .addKeyValue("errorMessage", e::getMessage)
                .log("Wrong listing title format");
            throw ValidationException.withPath("title", e);
        } catch (Throwable e) {
            log.atError()
                .setCause(e)
                .log("Failed to add new listing");
            throw e;
        }

        return listingId;
    }
}
