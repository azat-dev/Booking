package com.azat4dev.booking.listingsms.commands.domain.handers.modification;

import com.azat4dev.booking.listingsms.commands.domain.commands.UpdateListingDetails;
import com.azat4dev.booking.listingsms.commands.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listings;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.City;
import com.azat4dev.booking.listingsms.common.domain.values.address.Country;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.common.domain.values.address.Street;
import com.azat4dev.booking.shared.application.ValidationException;
import com.azat4dev.booking.shared.domain.DomainException;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Observed
@AllArgsConstructor
public class UpdateListingDetailsHandlerImpl implements UpdateListingDetailsHandler {

    private final Hosts hosts;
    private final Listings listings;

    @Override
    public void handle(UpdateListingDetails command) throws Exception.ListingNotFound, Exception.AccessForbidden, ValidationException {

        try {
            final var hostId = HostId.fromUserId(command.userId());
            final var host = hosts.getById(hostId);
            final var listingId = ListingId.checkAndMakeFrom(command.listingId());

            final var fields = command.fields();

            final var newTitle = fields.getTitle().map(ListingTitle::dangerouslyMakeFrom);

            final var newDescription = fields.getDescription()
                .map(v -> v.map(ListingDescription::dangerouslyMakeFrom));

            final var newCapacity = fields.getGuestsCapacity()
                .map(this::map);

            final var newPropertyType = fields.getPropertyType()
                .map(v -> v.map(PropertyType::valueOf));

            final var newRoomType = fields.getRoomType()
                .map(v -> v.map(RoomType::valueOf));

            final var newAddress = fields.getAddress()
                .map(nullableValue -> {

                    ListingAddress mappedValue = null;
                    
                    if (nullableValue.isPresent()) {
                        mappedValue = map(nullableValue.get());
                    }

                    return Optional.ofNullable(mappedValue);
                });

            final var listing = host.getListings()
                .findById(listingId)
                .orElseThrow(Listings.Exception.ListingNotFound::new);

            newTitle.ifPresent(listing::setTitle);
            newDescription.ifPresent(listing::setDescription);
            newCapacity.ifPresent(listing::setGuestsCapacity);

            newPropertyType.ifPresent(listing::setPropertyType);
            newRoomType.ifPresent(listing::setRoomType);
            newAddress.ifPresent(listing::setAddress);

            listings.update(listing);

        } catch (DomainException e) {
            mapException(e);
        }
    }

    private GuestsCapacity map(UpdateListingDetails.GuestsCapacity c) throws GuestsCapacity.Exception.CapacityMustBePositive {
        return GuestsCapacity.checkAndMake(
            c.adults(),
            c.children(),
            c.infants()
        );
    }

    private ListingAddress map(UpdateListingDetails.Address a) throws DomainException {
        return new ListingAddress(
            Country.checkAndMakeFrom(a.country()),
            City.checkAndMakeFrom(a.city()),
            Street.checkAndMakeFrom(a.street())
        );
    }

    void mapException(DomainException e) throws Exception.ListingNotFound {

        switch (e) {
            case ListingId.Exception inst:
                log.atWarn()
                    .addKeyValue("errorMessage", inst.getMessage())
                    .addKeyValue("errorCode", inst.getCode())
                    .log("Invalid listingId");

                throw ValidationException.withPath("listingId", inst);

            case ListingTitle.Exception inst:
                log.atWarn()
                    .addKeyValue("errorMessage", inst.getMessage())
                    .addKeyValue("errorCode", inst.getCode())
                    .log("Invalid title");
                throw ValidationException.withPath("title", inst);

            case ListingDescription.Exception inst:
                log.atWarn()
                    .addKeyValue("errorMessage", inst.getMessage())
                    .addKeyValue("errorCode", inst.getCode())
                    .log("Invalid description");

                throw ValidationException.withPath("description", inst);

            case Listings.Exception.ListingNotFound inst:

                log.atError()
                    .addKeyValue("errorMessage", inst.getMessage())
                    .log("Listing not found");
                throw new Exception.ListingNotFound();

            default:
                log.atError()
                    .setCause(e)
                    .log("Failed to update listing details");
                throw new RuntimeException(e);
        }
    }
}
