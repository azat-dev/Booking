package com.azat4dev.booking.listingsms.queries.presentation.api.resources;

import com.azat4dev.booking.listingsms.generated.server.api.QueriesPrivateApiDelegate;
import com.azat4dev.booking.listingsms.generated.server.model.GetListingPrivateDetailsResponse;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.application.handlers.GetListingPrivateDetailsHandler;
import com.azat4dev.booking.listingsms.queries.presentation.api.mappers.MapListingPrivateDetailsToDTO;
import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.presentation.CurrentAuthenticatedUserIdProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class PrivateListingsApi implements QueriesPrivateApiDelegate {

    private final CurrentAuthenticatedUserIdProvider currentUserId;
    private final MapListingPrivateDetailsToDTO mapToDTO;

    @Autowired
    GetListingPrivateDetailsHandler getListingPrivateDetailsHandler;

    @Override
    public ResponseEntity<GetListingPrivateDetailsResponse> getListingPrivateDetails(UUID listingId) {

        final var userId = currentUserId.get()
            .orElseThrow(() -> new ControllerException(HttpStatus.FORBIDDEN, "User not authenticated"));

        final var command = new GetListingPrivateDetails(
            userId,
            listingId.toString()
        );

        try {
            final var listing = getListingPrivateDetailsHandler.handle(command);

            final var responseData = new GetListingPrivateDetailsResponse(
                mapToDTO.map(listing)
            );

            return ResponseEntity.ok(responseData);
        } catch (GetListingPrivateDetailsHandler.ListingNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
