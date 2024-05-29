package com.azat4dev.booking.users.users_commands.domain.handlers.users.photo;

import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.PhotoFileExtension;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.MediaObjectName;
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
