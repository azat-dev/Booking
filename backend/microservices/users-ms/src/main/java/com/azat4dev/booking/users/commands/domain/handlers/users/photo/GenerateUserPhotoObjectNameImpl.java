package com.azat4dev.booking.users.commands.domain.handlers.users.photo;

import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.RequiredArgsConstructor;

import java.time.ZoneOffset;


@RequiredArgsConstructor
public final class GenerateUserPhotoObjectNameImpl implements GenerateUserPhotoObjectName {

    private final TimeProvider timeProvider;

    @Override
    public MediaObjectName execute(UserId userId, PhotoFileExtension extension) {

        final var timeStamp = timeProvider.currentTime().toInstant(ZoneOffset.UTC).toEpochMilli();

        String sb = userId.toString() +
                    "-" +
                    timeStamp + "." + extension.toString();

        return MediaObjectName.dangerouslyMake(sb);
    }
}
