package com.azat4dev.booking.listingsms.commands.domain.commands;

import com.azat4dev.booking.listingsms.commands.domain.values.OptionalField;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.*;

import java.util.Optional;

public record UpdateListingDetails(
    UserId userId,
    String listingId,
    Fields fields
) {

    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    @Builder
    public static final class Fields {

        private final OptionalField<Status> status;
        private final OptionalField<String> title;
        private final OptionalField<Optional<String>> description;
        private final OptionalField<Optional<String>> propertyType;
        private final OptionalField<Optional<String>> roomType;
        private final OptionalField<GuestsCapacity> guestsCapacity;
        private final OptionalField<Optional<Address>> address;
    }

    public record Address(
        String country,
        String city,
        String street
    ) {
    }

    public record GuestsCapacity(
        int adults,
        int children,
        int infants
    ) {
    }

    public enum Status {
        DRAFT,
        PUBLISHED,
        READY_FOR_PUBLISHING,
    }
}
