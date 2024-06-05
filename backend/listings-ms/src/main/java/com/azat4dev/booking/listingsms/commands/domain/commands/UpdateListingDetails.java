package com.azat4dev.booking.listingsms.commands.domain.commands;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Optional;

public record UpdateListingDetails(
    UserId userId,
    String listingId,
    Fields fields
) {

    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    public static final class Fields {
        private final Optional<String> title;
        private final Optional<Optional<String>> description;
        private final Optional<Optional<String>> propertyType;
        private final Optional<Optional<String>> roomType;
        private final Optional<GuestsCapacity> guestsCapacity;
        private final Optional<Optional<Address>> address;

        public static FieldsBuilder builder() {
            return new FieldsBuilder();
        }

        public static class FieldsBuilder {
            private Optional<String> title = Optional.empty();
            private Optional<Optional<String>> description = Optional.empty();
            private Optional<Optional<String>> propertyType = Optional.empty();
            private Optional<Optional<String>> roomType = Optional.empty();
            private Optional<GuestsCapacity> guestsCapacity = Optional.empty();
            private Optional<Optional<Address>> address = Optional.empty();

            FieldsBuilder() {
            }

            public FieldsBuilder title(Optional<String> title) {
                this.title = title;
                return this;
            }

            public FieldsBuilder description(Optional<Optional<String>> description) {
                this.description = description;
                return this;
            }

            public FieldsBuilder propertyType(Optional<Optional<String>> propertyType) {
                this.propertyType = propertyType;
                return this;
            }

            public FieldsBuilder roomType(Optional<Optional<String>> roomType) {
                this.roomType = roomType;
                return this;
            }

            public FieldsBuilder guestsCapacity(Optional<GuestsCapacity> guestsCapacity) {
                this.guestsCapacity = guestsCapacity;
                return this;
            }

            public FieldsBuilder address(Optional<Optional<Address>> address) {
                this.address = address;
                return this;
            }

            public Fields build() {
                return new Fields(this.title, this.description, this.propertyType, this.roomType, this.guestsCapacity, this.address);
            }
        }
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
}
