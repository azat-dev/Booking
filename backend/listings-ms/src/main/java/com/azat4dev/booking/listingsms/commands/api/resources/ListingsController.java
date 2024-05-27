package com.azat4dev.booking.listingsms.commands.api.resources;

import com.azat4dev.booking.listingsms.commands.api.dto.AddListingResponse;
import com.azat4dev.booking.listingsms.commands.application.handlers.AddNewListingHandler;
import com.azat4dev.booking.listingsms.commands.domain.commands.AddNewListing;
import com.azat4dev.booking.listingsms.commands.api.dto.AddListingRequest;
import com.azat4dev.booking.listingsms.queries.presentation.api.entities.GetListingPrivateDetailsResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class ListingsController implements ListingsResource {

    @Autowired
    private AddNewListingHandler addNewListingHandler;

    @Override
    public ResponseEntity<AddListingResponse> addNew(
        AddListingRequest body,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ValidateException {

        final var command = new AddNewListing(
            body.operationId(),
            UUID.randomUUID().toString(),
            body.title()
        );

        final var listingId = addNewListingHandler.handle(command);
        return ResponseEntity.ok(new AddListingResponse(listingId.getValue().toString()));
    }
}
