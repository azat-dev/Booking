package com.azat4dev.booking.listingsms.queries.presentation.api.resources;

import com.azat4dev.booking.listingsms.queries.presentation.api.entities.GetListingPrivateDetailsResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public class PrivateListingsQueriesController implements  PrivateListingsQueriesResource {
    @Override
    public ResponseEntity<GetListingPrivateDetailsResponse> getPrivateListing(String listingId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}
