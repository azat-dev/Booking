package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.commands.domain.events.ListingDetailsUpdated;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.OptionalField;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.generated.events.dto.*;
import com.azat4dev.booking.shared.data.serializers.Serializer;
import lombok.AllArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Optional;

@AllArgsConstructor
public final class MapListingDetailsUpdatedChange implements Serializer<ListingDetailsUpdated.Change, ListingDetailsFieldsDTO> {

    private final Serializer<GuestsCapacity, GuestsCapacityDTO> mapGuestCapacity;
    private final Serializer<ListingAddress, AddressDTO> mapAddress;

    private <T> JsonNullable<T> toNullable(OptionalField<T> field) {
        return field.isMissed() ? JsonNullable.undefined() : JsonNullable.of(field.get());
    }

    @Override
    public ListingDetailsFieldsDTO serialize(ListingDetailsUpdated.Change change) {
        final var title = change.title().map(ListingTitle::getValue);

        final var status = change.status()
            .map(Enum::name)
            .map(ListingStatusDTO::fromValue);

        final var description = change.description().map(v -> v.map(ListingDescription::getValue));

        final var propertyType = change.propertyType()
            .map(v -> v.map(Enum::name)
                .map(PropertyTypeDTO::valueOf));

        final var roomType = change.roomType()
            .map(v -> v.map(Enum::name)
                .map(RoomTypeDTO::valueOf));

        final var guestsCapacity = change.guestsCapacity()
            .map(mapGuestCapacity::serialize);

        final var address = change.address()
            .map(v -> v.map(mapAddress::serialize));

        return ListingDetailsFieldsDTO.builder()
            .status(toNullable(status))
            .title(toNullable(title))
            .description(toOptionalNullable(description))
            .propertyType(toOptionalNullable(propertyType))
            .roomType(toOptionalNullable(roomType))
            .guestCapacity(toNullable(guestsCapacity))
            .address(toOptionalNullable(address))
            .build();
    }

    private static <T> JsonNullable<T> toOptionalNullable(OptionalField<Optional<T>> field) {
        return field.isMissed() ? JsonNullable.undefined() : JsonNullable.of(field.get().orElse(null));
    }

    @Override
    public ListingDetailsUpdated.Change deserialize(ListingDetailsFieldsDTO dto) {
        return new ListingDetailsUpdated.Change(
            OptionalField.from(dto.getStatus()).map(Enum::name).map(ListingStatus::valueOf),
            OptionalField.from(dto.getTitle()).map(ListingTitle::dangerouslyMakeFrom),
            OptionalField.fromNullable(dto.getDescription()).map(v -> v.map(ListingDescription::dangerouslyMakeFrom)),
            OptionalField.fromNullable(dto.getPropertyType(), v -> PropertyType.valueOf(v.name())),
            OptionalField.fromNullable(dto.getRoomType(), v -> RoomType.valueOf(v.name())),
            OptionalField.from(dto.getGuestCapacity()).map(mapGuestCapacity::deserialize),
            OptionalField.fromNullable(dto.getAddress(), mapAddress::deserialize)
        );
    }

    @Override
    public Class<ListingDetailsUpdated.Change> getOriginalClass() {
        return ListingDetailsUpdated.Change.class;
    }

    @Override
    public Class<ListingDetailsFieldsDTO> getSerializedClass() {
        return ListingDetailsFieldsDTO.class;
    }
}