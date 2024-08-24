package com.azat4dev.booking.listingsms.queries.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.generated.api.bus.dto.listingsms.*;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

@AllArgsConstructor
public final class MapListingPublicDetails implements Serializer<ListingPublicDetails, ListingPublicDetailsDTO> {

    private final Serializer<ListingStatus, ListingStatusDTO> mapListingStatus;
    private final Serializer<ListingAddress, AddressDTO> mapListingAddress;
    private final Serializer<GuestsCapacity, GuestsCapacityDTO> mapGuestsCapacity;
    private final Serializer<PropertyType, PropertyTypeDTO> mapPropertyType;
    private final Serializer<RoomType, RoomTypeDTO> mapRoomType;
    private final Serializer<ListingPhoto, ListingPhotoDTO> mapListingPhoto;
    private final Serializer<LocalDateTime, String> mapLocalDateTime;

    @Override
    public ListingPublicDetailsDTO serialize(ListingPublicDetails dm) {
        return ListingPublicDetailsDTO.builder()
            .id(dm.id().getValue())
            .status(mapListingStatus.serialize(dm.status()))
            .createdAt(mapLocalDateTime.serialize(dm.createdAt()))
            .updatedAt(mapLocalDateTime.serialize(dm.updatedAt()))
            .title(dm.title().getValue())
            .description(dm.description().getValue())
            .address(mapListingAddress.serialize(dm.address()))
            .guestCapacity(mapGuestsCapacity.serialize(dm.guestsCapacity()))
            .propertyType(mapPropertyType.serialize(dm.propertyType()))
            .roomType(mapRoomType.serialize(dm.roomType()))
            .photos(dm.photos().stream().map(mapListingPhoto::serialize).toArray(ListingPhotoDTO[]::new))
            .build();
    }

    @Override
    public ListingPublicDetails deserialize(ListingPublicDetailsDTO dto) {
        try {
            return new ListingPublicDetails(
                ListingId.dangerouslyMakeFrom(dto.getId().toString()),
                mapLocalDateTime.deserialize(dto.getCreatedAt()),
                mapLocalDateTime.deserialize(dto.getUpdatedAt()),
                HostId.checkAndMakeFrom(dto.getRoomType().toString()),
                ListingTitle.checkAndMakeFrom(dto.getTitle()),
                mapListingStatus.deserialize(dto.getStatus()),
                ListingDescription.checkAndMakeFrom(dto.getDescription()),
                mapGuestsCapacity.deserialize(dto.getGuestCapacity()),
                mapPropertyType.deserialize(dto.getPropertyType()),
                mapRoomType.deserialize(dto.getRoomType()),
                mapListingAddress.deserialize(dto.getAddress()),
                Arrays.stream(dto.getPhotos()).map(mapListingPhoto::deserialize).toList()
            );
        } catch (DomainException e) {
            throw new Exception.FailedDeserialize(e);
        }
    }

    @Override
    public Class<ListingPublicDetails> getOriginalClass() {
        return ListingPublicDetails.class;
    }

    @Override
    public Class<ListingPublicDetailsDTO> getSerializedClass() {
        return ListingPublicDetailsDTO.class;
    }
}