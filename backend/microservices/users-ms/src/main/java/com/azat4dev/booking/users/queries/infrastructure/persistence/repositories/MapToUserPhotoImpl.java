package com.azat4dev.booking.users.queries.infrastructure.persistence.repositories;

import com.azat4dev.booking.shared.domain.values.BaseUrl;
import com.azat4dev.booking.users.queries.infrastructure.persistence.dao.records.UserRecord;
import com.azat4dev.booking.users.queries.domain.entities.UserPhoto;
import com.azat4dev.booking.users.queries.domain.interfaces.repositories.MapToUserPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;

@Slf4j
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
            log.atError()
                    .addArgument(photoPath)
                    .log("Invalid photo path: {}");
            throw new RuntimeException(e);
        }
    }
}
