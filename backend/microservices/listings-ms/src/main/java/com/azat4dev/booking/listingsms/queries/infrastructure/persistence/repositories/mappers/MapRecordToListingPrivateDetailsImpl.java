package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.commands.infrastructure.persistence.dao.listings.ListingPhotoData;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
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
public class MapRecordToListingPrivateDetailsImpl implements MapRecordToListingPrivateDetails {

    private final MapListingPhoto mapListingPhoto;
    private final MapAddress mapAddress;
    private final MapGuestsCapacity mapGuestsCapacity;
    private final ObjectMapper objectMapper;

    private List<ListingPhoto> mapPhotos(ListingsRecord data) {

        if (data.getPhotos() == null) {
            return List.of();
        }

        try {
            final var photos = objectMapper.readValue(data.getPhotos().data(), ListingPhotoData[].class);

            return Arrays.stream(photos).map(mapListingPhoto::map).toList();

        } catch (JsonProcessingException e) {
            log.atError()
                .setCause(e)
                .log("Failed to map photos from record");
            throw new RuntimeException(e);
        }
    }

    @Override
    public ListingPrivateDetails map(ListingsRecord data) {

        return new ListingPrivateDetails(
            ListingId.dangerouslyMakeFrom(data.getId().toString()),
            data.getCreatedAt().withNano(data.getCreatedAtNano()),
            data.getUpdatedAt().withNano(data.getUpdatedAtNano()),
            HostId.dangerouslyMakeFrom(data.getHostId().toString()),
            ListingTitle.dangerouslyMakeFrom(data.getTitle()),
            ListingStatus.valueOf(data.getStatus()),

            Optional.ofNullable(data.getDescription()).map(ListingDescription::dangerouslyMakeFrom),
            mapGuestsCapacity.map(data),
            Optional.ofNullable(data.getPropertyType()).map(PropertyType::valueOf),
            Optional.ofNullable(data.getRoomType()).map(RoomType::valueOf),

            mapAddress.map(data),
            mapPhotos(data)
        );
    }
}
