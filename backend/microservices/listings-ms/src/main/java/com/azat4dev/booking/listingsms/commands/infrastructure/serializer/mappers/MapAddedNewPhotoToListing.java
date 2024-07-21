package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;


import com.azat4dev.booking.listingsms.commands.domain.events.AddedNewPhotoToListing;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.generated.events.dto.AddedNewPhotoToListingDTO;
import com.azat4dev.booking.listingsms.generated.events.dto.ListingPhotoDTO;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapAddedNewPhotoToListing implements MapPayload<AddedNewPhotoToListing, AddedNewPhotoToListingDTO> {

    @Override
    public AddedNewPhotoToListingDTO toDTO(AddedNewPhotoToListing dm) {
        return AddedNewPhotoToListingDTO.builder()
            .listingId(dm.listingId().getValue())
            .photo(ListingPhotoDTO.builder()
                .id(dm.photo().getId())
                .bucketName(dm.photo().getBucketName().getValue())
                .objectName(dm.photo().getObjectName().getValue())
                .build()
            ).build();
    }

    @Override
    public AddedNewPhotoToListing toDomain(AddedNewPhotoToListingDTO dto) {
        return new AddedNewPhotoToListing(
            ListingId.dangerouslyMakeFrom(dto.getListingId().toString()),
            new ListingPhoto(
                dto.getPhoto().getId(),
                BucketName.makeWithoutChecks(dto.getPhoto().getBucketName()),
                MediaObjectName.dangerouslyMake(dto.getPhoto().getObjectName())
            )
        );
    }

    @Override
    public Class<AddedNewPhotoToListing> getDomainClass() {
        return AddedNewPhotoToListing.class;
    }

    @Override
    public Class<AddedNewPhotoToListingDTO> getDTOClass() {
        return AddedNewPhotoToListingDTO.class;
    }
}