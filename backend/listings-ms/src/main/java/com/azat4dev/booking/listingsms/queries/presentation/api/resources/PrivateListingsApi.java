package com.azat4dev.booking.listingsms.queries.presentation.api.resources;

import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.generated.server.api.QueriesPrivateApiDelegate;
import com.azat4dev.booking.listingsms.generated.server.model.GetListingPrivateDetailsResponse;
import com.azat4dev.booking.listingsms.generated.server.model.ListingPrivateDetailsDTO;
import com.azat4dev.booking.listingsms.queries.application.commands.GetListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.application.commands.GetOwnListings;
import com.azat4dev.booking.listingsms.queries.application.handlers.GetListingPrivateDetailsHandler;
import com.azat4dev.booking.listingsms.queries.application.handlers.GetOwnListingsHandler;
import com.azat4dev.booking.listingsms.queries.presentation.api.mappers.MapListingPrivateDetailsToDTO;
import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.presentation.CurrentAuthenticatedUserIdProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class PrivateListingsApi implements QueriesPrivateApiDelegate {

    private final CurrentAuthenticatedUserIdProvider currentUserId;
    private final MapListingPrivateDetailsToDTO mapToDTO;

    private final GetListingPrivateDetailsHandler getListingPrivateDetailsHandler;
    private final GetOwnListingsHandler getOwnListingsHandler;

    @Override
    public ResponseEntity<GetListingPrivateDetailsResponse> getListingPrivateDetails(UUID listingId) {

        final var userId = currentUserId.get()
            .orElseThrow(() -> new ControllerException(HttpStatus.FORBIDDEN, "Host not authenticated"));

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

    @Override
    public ResponseEntity<List<ListingPrivateDetailsDTO>> getOwnListings() {

        final var userId = currentUserId.get()
            .orElseThrow(() -> new ControllerException(HttpStatus.FORBIDDEN, "Host not authenticated"));

        final var hostId = HostId.fromUserId(userId);
        final var command = new GetOwnListings(hostId);

        try {

            final var listings = getOwnListingsHandler.handle(command)
                .stream().map(mapToDTO::map).toList();

            return ResponseEntity.ok(listings);

        } catch (GetOwnListingsHandler.Exception.Forbidden e) {
            throw new ControllerException(HttpStatus.FORBIDDEN, e);
        }
    }
}
