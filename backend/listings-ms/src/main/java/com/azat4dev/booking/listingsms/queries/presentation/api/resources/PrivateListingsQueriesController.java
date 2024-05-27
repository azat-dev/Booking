package com.azat4dev.booking.listingsms.queries.presentation.api.resources;

import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.application.handlers.GetListingPrivateDetailsHandler;
import com.azat4dev.booking.listingsms.queries.presentation.api.entities.GetListingPrivateDetailsResponse;
import com.azat4dev.booking.listingsms.queries.presentation.api.entities.ListingPrivateDetailsDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PrivateListingsQueriesController implements PrivateListingsQueriesResource {

    @Autowired
    GetListingPrivateDetailsHandler getListingPrivateDetailsHandler;

    @Override
    public ResponseEntity<GetListingPrivateDetailsResponse> getPrivateListing(String listingId, HttpServletRequest request, HttpServletResponse response) throws Exception {

        final var command = new GetListingPrivateDetails(listingId);
        final var privateListingDetails = getListingPrivateDetailsHandler.handle(command);

        final var responseData = new GetListingPrivateDetailsResponse(
            ListingPrivateDetailsDTO.fromDomain(privateListingDetails)
        );

        return ResponseEntity.ok(responseData);
    }
}
