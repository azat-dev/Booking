package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.commands.domain.events.ListingPublished;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.generated.events.dto.ListingPublishedDTO;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.shared.data.serializers.Mapper;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public final class MapListingPublished implements MapPayload<ListingPublished, ListingPublishedDTO> {

    private final Mapper<LocalDateTime, String> mapDateTime;

    @Override
    public ListingPublishedDTO toDTO(ListingPublished dm) {
        return ListingPublishedDTO.builder()
            .listingId(dm.listingId().getValue())
            .publishedAt(mapDateTime.toDTO(dm.publishedAt()))
            .build();
    }

    @Override
    public ListingPublished toDomain(ListingPublishedDTO dto) {
        return new ListingPublished(
            ListingId.dangerouslyMakeFrom(dto.getListingId().toString()),
            mapDateTime.toDomain(dto.getPublishedAt())
        );
    }

    @Override
    public Class<ListingPublished> getDomainClass() {
        return ListingPublished.class;
    }

    @Override
    public Class<ListingPublishedDTO> getDTOClass() {
        return ListingPublishedDTO.class;
    }
}