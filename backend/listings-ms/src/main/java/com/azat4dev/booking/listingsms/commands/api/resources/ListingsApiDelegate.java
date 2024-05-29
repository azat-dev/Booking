package com.azat4dev.booking.listingsms.commands.api.resources;

import com.azat4dev.booking.listingsms.commands.application.handlers.AddNewListingHandler;
import com.azat4dev.booking.listingsms.commands.domain.commands.AddNewListing;
import com.azat4dev.booking.listingsms.generated.server.api.CommandsApiDelegate;
import com.azat4dev.booking.listingsms.generated.server.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.generated.server.model.AddListingResponse;
import com.azat4dev.booking.listingsms.generated.server.model.GetPhotoUploadUrlForListingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ListingsApiDelegate implements CommandsApiDelegate {

    @Autowired
    AddNewListingHandler addNewListingHandler;

    @Override
    public ResponseEntity<AddListingResponse> addListing(AddListingRequestBody body) {

        final var command = new AddNewListing(
            body.getOperationId().toString(),
            UUID.randomUUID().toString(),
            body.getTitle()
        );

        final var listingId = addNewListingHandler.handle(command);
        return ResponseEntity.ok(new AddListingResponse(listingId.getValue()));
    }

    @Override
    public ResponseEntity<GetPhotoUploadUrlForListingResponse> getPhotoUploadUrl(UUID listingId, AddListingRequestBody addListingRequestBody) {
        return CommandsApiDelegate.super.getPhotoUploadUrl(listingId, addListingRequestBody);
    }
}
