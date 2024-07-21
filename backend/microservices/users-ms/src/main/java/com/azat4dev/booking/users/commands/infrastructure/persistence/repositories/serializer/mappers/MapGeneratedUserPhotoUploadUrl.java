package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.UploadFileFormData;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.GeneratedUserPhotoUploadUrl;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.usersms.generated.events.dto.GeneratedUserPhotoUploadUrlDTO;
import com.azat4dev.booking.usersms.generated.events.dto.UploadedFileFormDataDTO;
import lombok.AllArgsConstructor;

import java.net.MalformedURLException;
import java.net.URI;

@AllArgsConstructor
public final class MapGeneratedUserPhotoUploadUrl implements MapPayload<GeneratedUserPhotoUploadUrl, GeneratedUserPhotoUploadUrlDTO> {

    @Override
    public GeneratedUserPhotoUploadUrlDTO toDTO(GeneratedUserPhotoUploadUrl dm) {
        return GeneratedUserPhotoUploadUrlDTO.builder()
            .userId(dm.userId().toString())
            .formData(toDTO(dm.formData()))
            .build();
    }

    private static UploadedFileFormDataDTO toDTO(UploadFileFormData dm) {

        return UploadedFileFormDataDTO.builder()
            .url(URI.create(dm.url().toString()))
            .bucketName(dm.bucketName().getValue())
            .objectName(dm.objectName().getValue())
            .formData(dm.formData())
            .build();
    }

    @Override
    public GeneratedUserPhotoUploadUrl toDomain(GeneratedUserPhotoUploadUrlDTO dto) {
        return new GeneratedUserPhotoUploadUrl(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            toDomain(dto.getFormData())
        );
    }


    private UploadFileFormData toDomain(UploadedFileFormDataDTO dto) {
        try {
            return new UploadFileFormData(
                dto.getUrl().toURL(),
                BucketName.makeWithoutChecks(dto.getBucketName()),
                MediaObjectName.dangerouslyMake(dto.getObjectName()),
                dto.getFormData()
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<GeneratedUserPhotoUploadUrl> getDomainClass() {
        return GeneratedUserPhotoUploadUrl.class;
    }

    @Override
    public Class<GeneratedUserPhotoUploadUrlDTO> getDTOClass() {
        return com.azat4dev.booking.usersms.generated.events.dto.GeneratedUserPhotoUploadUrlDTO.class;
    }
}