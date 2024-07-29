package com.azat4dev.booking.listingsms.commands.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingFactory;
import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.commands.infrastructure.persistence.dao.listings.ListingPhotoData;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.common.domain.values.address.City;
import com.azat4dev.booking.listingsms.common.domain.values.address.Country;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.common.domain.values.address.Street;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.generated.tables.records.ListingsRecord;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class MapRecordToListingImpl implements MapRecordToListing {

    private final ListingFactory listingFactory;
    private final ObjectMapper objectMapper;

    private static Optional<ListingAddress> mapAddress(ListingsRecord data) {

        if (data.getAddressCountry() == null) {
            return Optional.empty();
        }

        return Optional.of(
            ListingAddress.dangerouslyMakeFrom(
                Country.dangerouslyMakeFrom(data.getAddressCountry()),
                City.dangerouslyMakeFrom(data.getAddressCity()),
                Street.dangerouslyMakeFrom(data.getAddressStreet())
            )
        );
    }

    private static GuestsCapacity mapGuestsCapacity(ListingsRecord data) {
        return GuestsCapacity.dangerouslyMake(
            data.getGuestsCapacityAdults(),
            data.getGuestsCapacityChildren(),
            data.getGuestsCapacityInfants()
        );
    }

    private List<ListingPhoto> mapPhotos(ListingsRecord data) {

        if (data.getPhotos() == null) {
            return List.of();
        }

        try {
            final var photos = objectMapper.readValue(data.getPhotos().data(), ListingPhotoData[].class);

            return Arrays.stream(photos).map(photo -> ListingPhoto.makeWithoutChecks(
                ListingPhotoId.makeWithoutChecks(photo.id()),
                BucketName.makeWithoutChecks(photo.bucketName()),
                MediaObjectName.dangerouslyMake(photo.objectName())
            )).toList();

        } catch (JsonProcessingException e) {
            log.atError()
                .addArgument(data.getId())
                .log("Failed to deserialize photos for listing with id: {}");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Listing map(ListingsRecord data) {

        return listingFactory.internalMake(
            ListingId.dangerouslyMakeFrom(data.getId().toString()),
            ListingStatus.valueOf(data.getStatus()),
            data.getCreatedAt().withNano(data.getCreatedAtNano()),
            data.getUpdatedAt().withNano(data.getUpdatedAtNano()),
            HostId.dangerouslyMakeFrom(data.getHostId().toString()),
            ListingTitle.dangerouslyMakeFrom(data.getTitle()),

            Optional.ofNullable(data.getDescription()).map(ListingDescription::dangerouslyMakeFrom),
            Optional.ofNullable(data.getPropertyType()).map(PropertyType::valueOf),
            Optional.ofNullable(data.getRoomType()).map(RoomType::valueOf),

            mapAddress(data),
            mapGuestsCapacity(data),
            mapPhotos(data)
        );
    }
}
