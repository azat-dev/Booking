package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.commands.domain.events.FailedGenerateUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.generated.api.bus.dto.listingsms.FailedGenerateUrlForUploadListingPhotoDTO;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public final class MapFailedGenerateUrlForUploadListingPhoto implements MapDomainEvent<FailedGenerateUrlForUploadListingPhoto, FailedGenerateUrlForUploadListingPhotoDTO> {

    @Override
    public FailedGenerateUrlForUploadListingPhotoDTO serialize(FailedGenerateUrlForUploadListingPhoto dm) {
        return FailedGenerateUrlForUploadListingPhotoDTO.builder()
                .operationId(UUID.fromString(dm.operationId().value()))
                .userId(dm.userId().value())
                .listingId(dm.listingId().getValue())
                .fileExtension(dm.fileExtension().toString())
                .fileSize(dm.fileSize())
                .build();
    }

    @Override
    public FailedGenerateUrlForUploadListingPhoto deserialize(FailedGenerateUrlForUploadListingPhotoDTO dto) {
        return new FailedGenerateUrlForUploadListingPhoto(
                IdempotentOperationId.dangerouslyMakeFrom(dto.getOperationId().toString()),
                UserId.dangerouslyMakeFrom(dto.getUserId().toString()),
                ListingId.dangerouslyMakeFrom(dto.getListingId().toString()),
                PhotoFileExtension.dangerouslyMakeFrom(dto.getFileExtension()),
                dto.getFileSize()
        );
    }

    @Override
    public Class<FailedGenerateUrlForUploadListingPhoto> getOriginalClass() {
        return FailedGenerateUrlForUploadListingPhoto.class;
    }

    @Override
    public Class<FailedGenerateUrlForUploadListingPhotoDTO> getSerializedClass() {
        return FailedGenerateUrlForUploadListingPhotoDTO.class;
    }
}