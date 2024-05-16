package com.azat4dev.demobooking.users.users_commands.domain.handlers.users;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.MediaObjectName;
import lombok.RequiredArgsConstructor;

import java.time.ZoneOffset;


@RequiredArgsConstructor
public final class GenerateUserPhotoObjectNameImpl implements GenerateUserPhotoObjectName {

    private final TimeProvider timeProvider;

    @Override
    public MediaObjectName execute(UserId userId) {

        final var timeStamp = timeProvider.currentTime().toInstant(ZoneOffset.UTC).toEpochMilli();

        String sb = userId.toString() +
                    "-" +
                    timeStamp;

        return MediaObjectName.checkAndMakeFrom(sb);
    }
}
