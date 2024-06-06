package com.azat4dev.booking.listingsms.commands.api.resources;

import com.azat4dev.booking.listingsms.commands.application.handlers.AddNewListingHandler;
import com.azat4dev.booking.listingsms.commands.domain.commands.AddNewListing;
import com.azat4dev.booking.listingsms.commands.domain.commands.UpdateListingDetails;
import com.azat4dev.booking.listingsms.commands.domain.handers.modification.UpdateListingDetailsHandler;
import com.azat4dev.booking.listingsms.commands.domain.values.OptionalField;
import com.azat4dev.booking.listingsms.generated.server.api.CommandsModificationsApiDelegate;
import com.azat4dev.booking.listingsms.generated.server.model.*;
import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.presentation.CurrentAuthenticatedUserIdProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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

        final var f = requestBody.getFields();

        final var commandFields = UpdateListingDetails.Fields.builder()
            .title(OptionalField.from(f.getTitle()))
            .description(OptionalField.fromNullable(f.getDescription()))
            .propertyType(OptionalField.fromNullable(f.getPropertyType(), Enum::name))
            .roomType(OptionalField.fromNullable(f.getRoomType(), Enum::name))
            .guestsCapacity(OptionalField.from(f.getGuestCapacity()).map(this::map))
            .address(OptionalField.fromNullable(f.getAddress(), this::map))
            .build();

        try {
            final var command = new UpdateListingDetails(
                userId,
                listingId.toString(),
                commandFields
            );

            updateListingDetailsHandler.handle(command);
            return ResponseEntity.ok().build();
        } catch (UpdateListingDetailsHandler.Exception.ListingNotFound e) {
            throw new ControllerException(org.springframework.http.HttpStatus.NOT_FOUND, e);
        } catch (UpdateListingDetailsHandler.Exception.AccessForbidden e) {
            throw new ControllerException(org.springframework.http.HttpStatus.FORBIDDEN, e);
        }
    }

    @Override
    public ResponseEntity<Void> publishListing(UUID listingId) {
        return CommandsModificationsApiDelegate.super.publishListing(listingId);
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
