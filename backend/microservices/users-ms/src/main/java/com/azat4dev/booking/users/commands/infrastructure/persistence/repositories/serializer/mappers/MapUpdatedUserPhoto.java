package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.commands.domain.core.events.UpdatedUserPhoto;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.usersms.generated.events.dto.UpdatedUserPhotoDTO;
import com.azat4dev.booking.usersms.generated.events.dto.UserPhotoPathDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapUpdatedUserPhoto implements MapPayload<UpdatedUserPhoto, UpdatedUserPhotoDTO> {

    private static UserPhotoPathDTO toDTO(UserPhotoPath dm) {
        return UserPhotoPathDTO.builder()
            .bucketName(dm.bucketName().getValue())
            .objectName(dm.objectName().getValue())
            .build();
    }

    @Override
    public UpdatedUserPhotoDTO toDTO(UpdatedUserPhoto dm) {
        return UpdatedUserPhotoDTO.builder()
            .userId(dm.userId().toString())
            .newPhotoPath(toDTO(dm.newPhotoPath()))
            .prevPhotoPath(dm.prevPhotoPath().map(MapUpdatedUserPhoto::toDTO))
            .build();
    }

    private UserPhotoPath toDomain(UserPhotoPathDTO dto) {
        return new UserPhotoPath(
            BucketName.makeWithoutChecks(dto.getBucketName()),
            MediaObjectName.dangerouslyMake(dto.getObjectName())
        );
    }

    @Override
    public UpdatedUserPhoto toDomain(UpdatedUserPhotoDTO dto) {
        return new UpdatedUserPhoto(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            toDomain(dto.getNewPhotoPath()),
            dto.getPrevPhotoPath().map(this::toDomain)
        );
    }

    @Override
    public Class<UpdatedUserPhoto> getDomainClass() {
        return UpdatedUserPhoto.class;
    }

    @Override
    public Class<UpdatedUserPhotoDTO> getDTOClass() {
        return UpdatedUserPhotoDTO.class;
    }
}