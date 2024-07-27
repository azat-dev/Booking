package com.azat4dev.booking.listingsms.queries.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPublicDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.PublicListings;
import com.azat4dev.booking.shared.application.ValidationException;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Observed
@AllArgsConstructor
public class GetListingPublicDetailsHandlerImpl implements GetListingPublicDetailsHandler {

    private final PublicListings publicListings;

    @Override
    public ListingPublicDetails handle(GetListingPublicDetails command) throws Exception {

        try {
            final var listingId = ListingId.checkAndMakeFrom(command.listingId());

            return publicListings.findById(listingId)
                .orElseThrow(() -> new Exception.ListingNotFound(listingId));

        } catch (ListingId.Exception.WrongFormat e) {
            throw ValidationException.withPath("listingId", e);
        } catch (PublicListings.Exception.ListingNotPublished e) {
            throw new Exception.Forbidden();
        }
    }
}
