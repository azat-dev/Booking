package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
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
) implements DomainEventPayload {

    public record Change(
        Optional<ListingStatus> status,
        Optional<ListingTitle> title,
        Optional<Optional<ListingDescription>> description,
        Optional<Optional<PropertyType>> propertyType,
        Optional<Optional<RoomType>> roomType,
        Optional<GuestsCapacity> guestsCapacity,
        Optional<Optional<ListingAddress>> address
    ) {

        private static <T> Optional<Optional<T>> ifChanged(Optional<T> oldValue, Optional<T> newValue) {

            if (oldValue.isEmpty() && newValue.isEmpty()) {
                return Optional.empty();
            }

            if (newValue.isEmpty() || oldValue.isEmpty()) {
                return Optional.of(newValue);
            }

            if (oldValue.get().equals(newValue.get())) {
                return Optional.empty();
            }

            return Optional.of(newValue);
        }

        private static <T> Optional<T> ifChangedNotOptional(T oldValue, T newValue) {

            if (oldValue.equals(newValue)) {
                return Optional.empty();
            }

            return Optional.of(newValue);
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
