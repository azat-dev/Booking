package com.azat4dev.booking.listingsms.commands.domain.handers.modification;

import com.azat4dev.booking.listingsms.commands.domain.commands.UpdateListingDetails;
import com.azat4dev.booking.listingsms.commands.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
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
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class UpdateListingDetailsHandlerImpl implements UpdateListingDetailsHandler {

    private final Hosts hosts;
    private final ListingsCatalog listings;

    @Override
    public void handle(UpdateListingDetails command) throws Exception.ListingNotFound, Exception.AccessForbidden, ValidationException {

        try {
            final var hostId = HostId.fromUserId(command.userId());
            final var host = hosts.getById(hostId);
            final var listingId = ListingId.checkAndMakeFrom(command.listingId());

            final var fields = command.fields();

            final var newTitle = fields.getTitle()
                .map(ListingTitle::dangerouslyMakeFrom);

            final var newDescription = fields.getDescription()
                .map(v -> v.map(ListingDescription::dangerouslyMakeFrom));

            Optional<GuestsCapacity> newCapacity = Optional.empty();

            final var capacity = fields.getGuestsCapacity();
            if (capacity.isPresent()) {
                newCapacity = Optional.of(map(capacity.get()));
            }

            final var newPropertyType = fields.getPropertyType()
                .map(v -> v.map(PropertyType::valueOf));

            final var newRoomType = fields.getRoomType()
                .map(v -> v.map(RoomType::valueOf));

            final var address = fields.getAddress();
            Optional<Optional<ListingAddress>> newAddress = Optional.empty();

            if (address.isPresent()) {
                final var addressValue = address.get();

                if (addressValue.isPresent()) {
                    newAddress = Optional.of(Optional.of(map(addressValue.get())));
                } else {
                    newAddress = Optional.of(Optional.empty());
                }
            }

            final var listing = host.getListings()
                .findById(listingId)
                .orElseThrow(ListingsCatalog.Exception.ListingNotFound::new);

            if (newTitle.isPresent()) {
                listing.setTitle(newTitle.get());
            }

            if (newPropertyType.isPresent()) {
                listing.setPropertyType(newPropertyType.get());
            }

            if (newDescription.isPresent()) {
                listing.setDescription(newDescription.get());
            }

            if (newRoomType.isPresent()) {
                listing.setRoomType(newRoomType.get());
            }

            if (newCapacity.isPresent()) {
                listing.setGuestsCapacity(newCapacity.get());
            }

            if (newAddress.isPresent()) {
                listing.setAddress(newAddress.get());
            }

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

    private ListingAddress map(UpdateListingDetails.Address a) throws Country.Exception.TooLong, Country.Exception.NotBlank, Country.Exception.NotEmpty, City.Exception.TooLong, City.Exception.NotBlank, City.Exception.NotEmpty, Street.Exception.TooLong, Street.Exception.NotBlank, Street.Exception.NotEmpty {
        return new ListingAddress(
            Country.checkAndMakeFrom(a.country()),
            City.checkAndMakeFrom(a.city()),
            Street.checkAndMakeFrom(a.street())
        );
    }

    void mapException(DomainException e) throws Exception.ListingNotFound {

        switch (e) {
            case ListingId.Exception inst:
                throw ValidationException.withPath("listingId", inst);

            case ListingTitle.Exception inst:
                throw ValidationException.withPath("title", inst);

            case ListingDescription.Exception inst:
                throw ValidationException.withPath("description", inst);

            case ListingsCatalog.Exception.ListingNotFound inst:
                throw new Exception.ListingNotFound();

            default:
                throw new RuntimeException(e);
        }
    }
}
