package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.UploadedFileData;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.FailedUpdateUserPhoto;
import com.azat4dev.booking.usersms.generated.api.bus.dto.usersms.FailedUpdateUserPhotoDTO;
import com.azat4dev.booking.usersms.generated.api.bus.dto.usersms.UploadedFileDataDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapFailedUpdateUserPhoto implements MapDomainEvent<FailedUpdateUserPhoto, FailedUpdateUserPhotoDTO> {

    @Override
    public FailedUpdateUserPhotoDTO serialize(FailedUpdateUserPhoto dm) {
        return FailedUpdateUserPhotoDTO.builder()
            .operationId(dm.operationId().toString())
            .userId(dm.userId().toString())
            .uploadedFileData(toDTO(dm.uploadedFileData()))
            .build();
    }

    @Override
    public FailedUpdateUserPhoto deserialize(FailedUpdateUserPhotoDTO dto) {
        return new FailedUpdateUserPhoto(
            IdempotentOperationId.dangerouslyMakeFrom(dto.getOperationId()),
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            toDomain(dto.getUploadedFileData())
        );
    }

    private static UploadedFileData toDomain(UploadedFileDataDTO dto) {
        return new UploadedFileData(
            BucketName.makeWithoutChecks(dto.getBucketName()),
            MediaObjectName.dangerouslyMake(dto.getObjectName())
        );
    }

    private static UploadedFileDataDTO toDTO(UploadedFileData dm) {
        return UploadedFileDataDTO.builder()
            .bucketName(dm.bucketName().getValue())
            .objectName(dm.objectName().getValue())
            .build();
    }

    @Override
    public Class<FailedUpdateUserPhoto> getOriginalClass() {
        return FailedUpdateUserPhoto.class;
    }

    @Override
    public Class<FailedUpdateUserPhotoDTO> getSerializedClass() {
        return FailedUpdateUserPhotoDTO.class;
    }
}
