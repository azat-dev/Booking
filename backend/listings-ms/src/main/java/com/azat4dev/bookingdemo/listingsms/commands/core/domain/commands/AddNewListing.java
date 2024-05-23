package com.azat4dev.bookingdemo.listingsms.commands.core.domain.commands;

import com.azat4dev.bookingdemo.listingsms.commands.core.domain.values.ListingDescription;
import com.azat4dev.bookingdemo.listingsms.commands.core.domain.values.ListingId;
import com.azat4dev.bookingdemo.listingsms.commands.core.domain.values.ListingTitle;
import com.azat4dev.bookingdemo.listingsms.commands.core.domain.values.OwnerId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddNewListing {

    public final ListingId listingId;
    public final OwnerId ownerId;
    public final ListingTitle title;
    public final ListingDescription description;
}
