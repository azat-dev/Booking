package com.azat4dev.booking.listingsms.queries.presentation.api.resources;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.generated.server.api.QueriesApiDelegate;
import com.azat4dev.booking.listingsms.generated.server.model.GetListingPrivateDetailsResponse;
import com.azat4dev.booking.listingsms.generated.server.model.ListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.application.handlers.GetListingPrivateDetailsHandler;
import com.azat4dev.booking.listingsms.queries.domain.entities.PrivateListingDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PrivateListingsQueriesDelegate implements QueriesApiDelegate {

    @Autowired
    GetListingPrivateDetailsHandler getListingPrivateDetailsHandler;

    @Override
    public ResponseEntity<GetListingPrivateDetailsResponse> getListingPrivateDetails(UUID listingId) {

        final var command = new GetListingPrivateDetails(listingId.toString());
        final PrivateListingDetails privateListingDetails;
        try {
            privateListingDetails = getListingPrivateDetailsHandler.handle(command);
        } catch (GetListingPrivateDetailsHandler.ListingNotFoundException e) {
            throw new RuntimeException(e);
        }

        final var description = privateListingDetails.description()
            .map(ListingDescription::getValue).orElse(null);

        final var responseData = new GetListingPrivateDetailsResponse(
            new ListingPrivateDetails(
                privateListingDetails.id().getValue(),
                privateListingDetails.title().getValue(),
                description
            )
        );

        return ResponseEntity.ok(responseData);
    }
}
