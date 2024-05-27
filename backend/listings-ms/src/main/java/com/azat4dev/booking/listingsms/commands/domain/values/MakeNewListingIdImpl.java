package com.azat4dev.booking.listingsms.commands.domain.values;

import java.util.UUID;

public final class MakeNewListingIdImpl implements MakeNewListingId {

    @Override
    public ListingId make() {
        return ListingId.checkAndMakeFrom(UUID.randomUUID().toString());
    }
}
