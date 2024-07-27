package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.commands.infrastructure.persistence.dao.listings.ListingPhotoData;
import com.azat4dev.booking.listingsms.common.domain.values.PropertyType;
import com.azat4dev.booking.listingsms.common.domain.values.RoomType;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.generated.tables.records.ListingsRecord;

import java.util.Arrays;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class MapRecordToListingPublicDetailsImpl implements MapRecordToListingPublicDetails {

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
    public ListingPublicDetails map(ListingsRecord data) {

        final var id = ListingId.dangerouslyMakeFrom(data.getId().toString());
        final var status = ListingStatus.valueOf(data.getStatus());

        if (status != ListingStatus.PUBLISHED) {
            throw new Exception.ListingNotPublished(id);
        }

        return new ListingPublicDetails(
            id,
            data.getCreatedAt().withNano(data.getCreatedAtNano()),
            data.getUpdatedAt().withNano(data.getUpdatedAtNano()),
            HostId.dangerouslyMakeFrom(data.getHostId().toString()),
            ListingTitle.dangerouslyMakeFrom(data.getTitle()),
            status,

            ListingDescription.dangerouslyMakeFrom(data.getDescription()),
            mapGuestsCapacity.map(data),
            PropertyType.valueOf(data.getPropertyType()),
            RoomType.valueOf(data.getRoomType()),

            mapAddress.map(data).orElseThrow(() -> new RuntimeException("Address is required")),
            mapPhotos(data)
        );
    }
}
