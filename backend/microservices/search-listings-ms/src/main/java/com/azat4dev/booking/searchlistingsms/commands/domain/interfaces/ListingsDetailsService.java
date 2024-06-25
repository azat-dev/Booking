package com.azat4dev.booking.searchlistingsms.commands.domain.interfaces;

import com.azat4dev.booking.searchlistingsms.commands.domain.values.ListingId;

public interface ListingsDetailsService {

    ListingDetails getDetailsFor(ListingId listingId);
}
