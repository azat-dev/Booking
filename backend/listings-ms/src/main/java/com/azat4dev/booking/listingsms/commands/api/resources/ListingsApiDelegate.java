package com.azat4dev.booking.listingsms.commands.api.resources;

import com.azat4dev.booking.listingsms.commands.application.handlers.AddNewListingHandler;
import com.azat4dev.booking.listingsms.commands.domain.commands.AddNewListing;
import com.azat4dev.booking.listingsms.generated.server.api.CommandsModificationsApiDelegate;
import com.azat4dev.booking.listingsms.generated.server.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.generated.server.model.AddListingResponse;
import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.presentation.CurrentAuthenticatedUserIdProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListingsApiDelegate implements CommandsModificationsApiDelegate {

    private final CurrentAuthenticatedUserIdProvider currentUserId;
    private final AddNewListingHandler addNewListingHandler;

    @Override
    public ResponseEntity<AddListingResponse> addListing(AddListingRequestBody body) {

        final var userId = currentUserId.get()
            .orElseThrow(() -> new ControllerException(
                org.springframework.http.HttpStatus.UNAUTHORIZED,
                "Host not authenticated"
            ));

        final var command = new AddNewListing(
            body.getOperationId().toString(),
            userId.value().toString(),
            body.getTitle()
        );

        final var listingId = addNewListingHandler.handle(command);
        return ResponseEntity.ok(new AddListingResponse(listingId.getValue()));
    }
}
