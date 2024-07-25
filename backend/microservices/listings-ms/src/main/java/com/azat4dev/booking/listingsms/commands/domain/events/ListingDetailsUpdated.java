package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

import java.time.LocalDateTime;
import java.util.Optional;

public record ListingDetailsUpdated(
    ListingId listingId,
    LocalDateTime updatedAt,
    Change previousState,
    Change newState
) implements DomainEventPayload, EventWithListingId {

    public record Change(
        OptionalField<ListingStatus> status,
        OptionalField<ListingTitle> title,
        OptionalField<Optional<ListingDescription>> description,
        OptionalField<Optional<PropertyType>> propertyType,
        OptionalField<Optional<RoomType>> roomType,
        OptionalField<GuestsCapacity> guestsCapacity,
        OptionalField<Optional<ListingAddress>> address
    ) {

        private static <T> OptionalField<Optional<T>> ifChanged(Optional<T> oldValue, Optional<T> newValue) {

            if (oldValue.isEmpty() && newValue.isEmpty()) {
                return OptionalField.missed();
            }

            if (newValue.isEmpty() || oldValue.isEmpty()) {
                return OptionalField.present(newValue);
            }

            if (oldValue.get().equals(newValue.get())) {
                return OptionalField.missed();
            }

            return OptionalField.present(newValue);
        }

        private static <T> OptionalField<T> ifChangedNotOptional(T oldValue, T newValue) {

            if (oldValue.equals(newValue)) {
                return OptionalField.missed();
            }

            return OptionalField.present(newValue);
        }

        public static Change from(Listing baseState, Listing updatedState) {
            return new Change(
                ifChangedNotOptional(baseState.getStatus(), updatedState.getStatus()),
                ifChangedNotOptional(baseState.getTitle(), updatedState.getTitle()),
                ifChanged(baseState.getDescription(), updatedState.getDescription()),
                ifChanged(baseState.getPropertyType(), updatedState.getPropertyType()),
                ifChanged(baseState.getRoomType(), updatedState.getRoomType()),
                ifChangedNotOptional(baseState.getGuestsCapacity(), updatedState.getGuestsCapacity()),
                ifChanged(baseState.getAddress(), updatedState.getAddress())
            );
        }
    }
}
