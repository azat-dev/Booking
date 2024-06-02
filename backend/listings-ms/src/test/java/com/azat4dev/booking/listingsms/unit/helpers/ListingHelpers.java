package com.azat4dev.booking.listingsms.unit.helpers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import com.azat4dev.booking.shared.domain.values.user.UserId;

import java.util.UUID;

public class ListingHelpers {

    public static ListingId anyListingId() {
        try {
            return ListingId.checkAndMakeFrom(UUID.randomUUID().toString());
        } catch (ListingId.Exception.WrongFormat e) {
            throw new RuntimeException(e);
        }
    }

    public static UserId anyUserId() {
        try {
            return UserId.fromUUID(UUID.randomUUID());
        } catch (UserId.WrongFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static OwnerId anyOwnerId() {
        return OwnerId.checkAndMakeFrom(UUID.randomUUID().toString());
    }
}
