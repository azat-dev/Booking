package com.azat4dev.booking.listingsms.commands.domain.handers.photo;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import com.azat4dev.booking.shared.domain.values.files.PhotoFileExtension;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.RequiredArgsConstructor;

import java.time.ZoneOffset;


@RequiredArgsConstructor
public final class GenerateObjectNameForListingPhotoImpl implements GenerateObjectNameForListingPhoto {

    private final TimeProvider timeProvider;

    @Override
    public MediaObjectName execute(UserId userId, ListingId listingId, PhotoFileExtension extension) {

        final var timeStamp = timeProvider.currentTime().toInstant(ZoneOffset.UTC).toEpochMilli();

        String sb = listingId.toString() +
                    "-" +
                    timeStamp + "." + extension.toString();

        return MediaObjectName.dangerouslyMake(sb);
    }
}
