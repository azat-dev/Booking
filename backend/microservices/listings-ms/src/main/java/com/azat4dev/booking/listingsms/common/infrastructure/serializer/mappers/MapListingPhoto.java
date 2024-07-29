package com.azat4dev.booking.listingsms.common.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhotoId;
import com.azat4dev.booking.listingsms.generated.events.dto.ListingPhotoDTO;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapListingPhoto implements Serializer<ListingPhoto, ListingPhotoDTO> {
    @Override
    public ListingPhotoDTO serialize(ListingPhoto dm) {
        return ListingPhotoDTO.builder()
            .id(dm.getId().toString())
            .bucketName(dm.getBucketName().getValue())
            .objectName(dm.getObjectName().getValue())
            .build();
    }

    @Override
    public ListingPhoto deserialize(ListingPhotoDTO dto) {
        try {
            return new ListingPhoto(
                ListingPhotoId.checkAndMake(dto.getId()),
                BucketName.checkAndMake(dto.getBucketName()),
                MediaObjectName.checkAndMakeFrom(dto.getObjectName())
            );
        } catch (DomainException e) {
            throw new Exception.FailedDeserialize(e);
        }
    }

    @Override
    public Class<ListingPhoto> getOriginalClass() {
        return ListingPhoto.class;
    }

    @Override
    public Class<ListingPhotoDTO> getSerializedClass() {
        return ListingPhotoDTO.class;
    }
}