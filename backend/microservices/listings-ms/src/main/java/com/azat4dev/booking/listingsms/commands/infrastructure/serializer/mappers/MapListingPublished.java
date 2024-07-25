package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.commands.domain.events.ListingPublished;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.generated.events.dto.ListingPublishedDTO;
import com.azat4dev.booking.shared.data.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.data.serializers.Serializer;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public final class MapListingPublished implements MapDomainEvent<ListingPublished, ListingPublishedDTO> {

    private final Serializer<LocalDateTime, String> mapDateTime;

    @Override
    public ListingPublishedDTO serialize(ListingPublished dm) {
        return ListingPublishedDTO.builder()
            .listingId(dm.listingId().getValue())
            .publishedAt(mapDateTime.serialize(dm.publishedAt()))
            .build();
    }

    @Override
    public ListingPublished deserialize(ListingPublishedDTO dto) {
        return new ListingPublished(
            ListingId.dangerouslyMakeFrom(dto.getListingId().toString()),
            mapDateTime.deserialize(dto.getPublishedAt())
        );
    }

    @Override
    public Class<ListingPublished> getOriginalClass() {
        return ListingPublished.class;
    }

    @Override
    public Class<ListingPublishedDTO> getSerializedClass() {
        return ListingPublishedDTO.class;
    }
}