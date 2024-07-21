package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;


import com.azat4dev.booking.listingsms.commands.domain.events.GeneratedUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.generated.events.dto.GeneratedUrlForUploadListingPhotoDTO;
import com.azat4dev.booking.listingsms.generated.events.dto.UploadFileFormDataDTO;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.UploadFileFormData;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.AllArgsConstructor;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

@AllArgsConstructor
public final class MapGeneratedUrlForUploadListingPhoto implements MapPayload<GeneratedUrlForUploadListingPhoto, GeneratedUrlForUploadListingPhotoDTO> {

    @Override
    public GeneratedUrlForUploadListingPhotoDTO toDTO(GeneratedUrlForUploadListingPhoto dm) {
        return GeneratedUrlForUploadListingPhotoDTO.builder()
            .userId(dm.userId().value())
            .listingId(dm.listingId().getValue())
            .formData(toDTO(dm.formData()))
            .build();
    }

    @Override
    public GeneratedUrlForUploadListingPhoto toDomain(GeneratedUrlForUploadListingPhotoDTO dto) {
        return new GeneratedUrlForUploadListingPhoto(
            UserId.dangerouslyMakeFrom(dto.getUserId().toString()),
            ListingId.dangerouslyMakeFrom(dto.getListingId().toString()),
            toDomain(dto.getFormData())
        );
    }

    private static UploadFileFormDataDTO toDTO(UploadFileFormData formData) {
        try {
            return UploadFileFormDataDTO.builder()
                .url(formData.url().toURI())
                .bucketName(formData.bucketName().toString())
                .objectName(formData.objectName().toString())
                .fields(formData.formData())
                .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static UploadFileFormData toDomain(UploadFileFormDataDTO formData) {
        try {
            return new UploadFileFormData(
                formData.getUrl().toURL(),
                BucketName.makeWithoutChecks(formData.getBucketName()),
                MediaObjectName.dangerouslyMake(formData.getObjectName()),
                formData.getFields()
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<GeneratedUrlForUploadListingPhoto> getDomainClass() {
        return GeneratedUrlForUploadListingPhoto.class;
    }

    @Override
    public Class<GeneratedUrlForUploadListingPhotoDTO> getDTOClass() {
        return GeneratedUrlForUploadListingPhotoDTO.class;
    }
}