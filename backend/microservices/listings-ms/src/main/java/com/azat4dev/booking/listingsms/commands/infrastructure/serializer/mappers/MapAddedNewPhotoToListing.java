package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;


import com.azat4dev.booking.listingsms.commands.domain.events.AddedNewPhotoToListing;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhotoId;
import com.azat4dev.booking.listingsms.generated.api.bus.dto.listingsms.AddedNewPhotoToListingDTO;
import com.azat4dev.booking.listingsms.generated.api.bus.dto.listingsms.ListingPhotoDTO;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapAddedNewPhotoToListing implements MapDomainEvent<AddedNewPhotoToListing, AddedNewPhotoToListingDTO> {

    @Override
    public AddedNewPhotoToListingDTO serialize(AddedNewPhotoToListing dm) {
        return AddedNewPhotoToListingDTO.builder()
            .listingId(dm.listingId().getValue())
            .photo(ListingPhotoDTO.builder()
                .id(dm.photo().getId().toString())
                .bucketName(dm.photo().getBucketName().getValue())
                .objectName(dm.photo().getObjectName().getValue())
                .build()
            ).build();
    }

    @Override
    public AddedNewPhotoToListing deserialize(AddedNewPhotoToListingDTO dto) {
        return new AddedNewPhotoToListing(
            ListingId.dangerouslyMakeFrom(dto.getListingId().toString()),
            new ListingPhoto(
                ListingPhotoId.makeWithoutChecks(dto.getPhoto().getId()),
                BucketName.makeWithoutChecks(dto.getPhoto().getBucketName()),
                MediaObjectName.dangerouslyMake(dto.getPhoto().getObjectName())
            )
        );
    }

    @Override
    public Class<AddedNewPhotoToListing> getOriginalClass() {
        return AddedNewPhotoToListing.class;
    }

    @Override
    public Class<AddedNewPhotoToListingDTO> getSerializedClass() {
        return AddedNewPhotoToListingDTO.class;
    }
}