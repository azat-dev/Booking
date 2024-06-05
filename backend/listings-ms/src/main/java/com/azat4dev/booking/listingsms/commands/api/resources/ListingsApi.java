package com.azat4dev.booking.listingsms.commands.api.resources;

import com.azat4dev.booking.listingsms.commands.application.handlers.AddNewListingHandler;
import com.azat4dev.booking.listingsms.commands.domain.commands.AddNewListing;
import com.azat4dev.booking.listingsms.commands.domain.commands.UpdateListingDetails;
import com.azat4dev.booking.listingsms.commands.domain.handers.modification.UpdateListingDetailsHandler;
import com.azat4dev.booking.listingsms.generated.server.api.CommandsModificationsApiDelegate;
import com.azat4dev.booking.listingsms.generated.server.model.*;
import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.presentation.CurrentAuthenticatedUserIdProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ListingsApi implements CommandsModificationsApiDelegate {

    private final CurrentAuthenticatedUserIdProvider currentUserId;
    private final AddNewListingHandler addNewListingHandler;
    private final UpdateListingDetailsHandler updateListingDetailsHandler;


    @Override
    public ResponseEntity<AddListingResponseDTO> addListing(AddListingRequestBodyDTO body) {

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
        return ResponseEntity.ok(new AddListingResponseDTO(listingId.getValue()));
    }


    @Override
    public ResponseEntity<Void> updateListingDetails(UUID listingId, UpdateListingDetailsRequestBodyDTO requestBody) {

        final var userId = currentUserId.get().orElseThrow(() -> new ControllerException(
            org.springframework.http.HttpStatus.UNAUTHORIZED,
            "Host not authenticated"
        ));

        final var inputFields = requestBody.getFields();
        final var fb = UpdateListingDetails.Fields.builder();

        if (inputFields.getTitle().isPresent()) {
            fb.title(Optional.of(inputFields.getTitle().get()));
        }

        if (inputFields.getDescription().isPresent()) {
            fb.description(Optional.of(Optional.of(inputFields.getDescription().get())));
        }

        if (inputFields.getPropertyType().isPresent()) {
            final var propertyType = Optional.of(inputFields.getPropertyType().get()).map(Enum::name);
            fb.propertyType(Optional.of(propertyType));
        }

        if (inputFields.getRoomType().isPresent()) {
            final var roomType = Optional.of(inputFields.getRoomType().get()).map(Enum::name);
            fb.roomType(Optional.of(roomType));
        }

        if (inputFields.getGuestCapacity().isPresent()) {
            final var guestCapacity = this.map(inputFields.getGuestCapacity().get());
            fb.guestsCapacity(Optional.of(guestCapacity));
        }

        if (inputFields.getAddress().isPresent()) {
            final var address = Optional.of(inputFields.getAddress().get()).map(this::map);
            fb.address(Optional.of(address));
        }

        try {
            final var command = new UpdateListingDetails(
                userId,
                listingId.toString(),
                fb.build()
            );

            updateListingDetailsHandler.handle(command);
            return ResponseEntity.ok().build();
        } catch (UpdateListingDetailsHandler.Exception.ListingNotFound e) {
            throw new ControllerException(org.springframework.http.HttpStatus.NOT_FOUND, e);
        } catch (UpdateListingDetailsHandler.Exception.AccessForbidden e) {
            throw new ControllerException(org.springframework.http.HttpStatus.FORBIDDEN, e);
        }
    }

    private UpdateListingDetails.GuestsCapacity map(GuestsCapacityDTO dto) {
        return new UpdateListingDetails.GuestsCapacity(
            dto.getAdults(),
            dto.getChildren(),
            dto.getInfants()
        );
    }

    private UpdateListingDetails.Address map(AddressDTO dto) {
        return new UpdateListingDetails.Address(
            dto.getCountry(),
            dto.getCity(),
            dto.getStreet()
        );
    }
}
