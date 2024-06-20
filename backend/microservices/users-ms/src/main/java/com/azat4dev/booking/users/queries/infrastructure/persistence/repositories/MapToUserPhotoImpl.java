package com.azat4dev.booking.users.queries.infrastructure.persistence.repositories;

import com.azat4dev.booking.shared.domain.values.BaseUrl;
import com.azat4dev.booking.users.queries.infrastructure.persistence.dao.records.UserRecord;
import com.azat4dev.booking.users.queries.domain.entities.UserPhoto;
import com.azat4dev.booking.users.queries.domain.interfaces.repositories.MapToUserPhoto;
import lombok.RequiredArgsConstructor;

import java.net.MalformedURLException;

@RequiredArgsConstructor
public final class MapToUserPhotoImpl implements MapToUserPhoto {

    private final BaseUrl baseUrl;

    @Override
    public UserPhoto map(UserRecord.PhotoPath photoPath) {
        try {
            return new UserPhoto(
                baseUrl.urlWithPath(photoPath.bucketName() + "/" + photoPath.objectName())
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
