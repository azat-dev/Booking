package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.events.FailedGenerateUserPhotoUploadUrl;
import com.azat4dev.booking.usersms.generated.events.dto.FailedGenerateUserPhotoUploadUrlDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapFailedGenerateUserPhotoUploadUrl implements MapPayload<FailedGenerateUserPhotoUploadUrl, FailedGenerateUserPhotoUploadUrlDTO> {

    @Override
    public FailedGenerateUserPhotoUploadUrlDTO toDTO(FailedGenerateUserPhotoUploadUrl dm) {
        return FailedGenerateUserPhotoUploadUrlDTO.builder()
            .userId(dm.userId().toString())
            .operationId(dm.operationId().toString())
            .fileExtension(dm.fileExtension().toString())
            .fileSize(dm.fileSize())
            .build();
    }

    @Override
    public FailedGenerateUserPhotoUploadUrl toDomain(FailedGenerateUserPhotoUploadUrlDTO dto) {
        return new FailedGenerateUserPhotoUploadUrl(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            PhotoFileExtension.dangerouslyMakeFrom(dto.getFileExtension()),
            dto.getFileSize(),
            IdempotentOperationId.dangerouslyMakeFrom(dto.getOperationId())
        );
    }

    @Override
    public Class<FailedGenerateUserPhotoUploadUrl> getDomainClass() {
        return FailedGenerateUserPhotoUploadUrl.class;
    }

    @Override
    public Class<FailedGenerateUserPhotoUploadUrlDTO> getDTOClass() {
        return FailedGenerateUserPhotoUploadUrlDTO.class;
    }
}