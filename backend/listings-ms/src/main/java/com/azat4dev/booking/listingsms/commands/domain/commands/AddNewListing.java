package com.azat4dev.booking.listingsms.commands.domain.commands;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import com.azat4dev.booking.shared.domain.event.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddNewListing implements Command {

    public final ListingId listingId;
    public final OwnerId ownerId;
    public final ListingTitle title;
}
