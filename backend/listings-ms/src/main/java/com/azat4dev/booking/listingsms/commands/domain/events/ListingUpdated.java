package com.azat4dev.booking.listingsms.commands.domain.events;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public final class ListingUpdated implements DomainEventPayload {
    private final ListingId listingId;
    private final LocalDateTime updatedAt;
    private final State previousState;
    private final State newState;


    @Builder
    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    @ToString
    public static final class State {
        private final ListingStatus status;
        private final ListingTitle title;
        private final Optional<ListingDescription> description;
        private final Optional<PropertyType> propertyType;
        private final Optional<RoomType> roomType;
        private final GuestsCapacity guestsCapacity;
        private final Optional<ListingAddress> address;

    }
}
