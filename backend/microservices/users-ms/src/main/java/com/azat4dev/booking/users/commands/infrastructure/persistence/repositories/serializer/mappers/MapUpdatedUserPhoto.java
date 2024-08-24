package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.serializer.mappers;

import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.commands.domain.core.events.UpdatedUserPhoto;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import com.azat4dev.booking.usersms.generated.api.bus.dto.usersms.UpdatedUserPhotoDTO;
import com.azat4dev.booking.usersms.generated.api.bus.dto.usersms.UserPhotoPathDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapUpdatedUserPhoto implements MapDomainEvent<UpdatedUserPhoto, UpdatedUserPhotoDTO> {

    private static UserPhotoPathDTO toDTO(UserPhotoPath dm) {
        return UserPhotoPathDTO.builder()
            .bucketName(dm.bucketName().getValue())
            .objectName(dm.objectName().getValue())
            .build();
    }

    @Override
    public UpdatedUserPhotoDTO serialize(UpdatedUserPhoto dm) {
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
    public UpdatedUserPhoto deserialize(UpdatedUserPhotoDTO dto) {
        return new UpdatedUserPhoto(
            UserId.dangerouslyMakeFrom(dto.getUserId()),
            toDomain(dto.getNewPhotoPath()),
            dto.getPrevPhotoPath().map(this::toDomain)
        );
    }

    @Override
    public Class<UpdatedUserPhoto> getOriginalClass() {
        return UpdatedUserPhoto.class;
    }

    @Override
    public Class<UpdatedUserPhotoDTO> getSerializedClass() {
        return UpdatedUserPhotoDTO.class;
    }
}