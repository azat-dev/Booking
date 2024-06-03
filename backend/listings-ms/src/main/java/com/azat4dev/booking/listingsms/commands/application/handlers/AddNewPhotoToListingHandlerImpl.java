package com.azat4dev.booking.listingsms.commands.application.handlers;

import com.azat4dev.booking.listingsms.commands.application.commands.AddNewPhotoToListing;
import com.azat4dev.booking.listingsms.commands.domain.entities.Hosts;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class AddNewPhotoToListingHandlerImpl implements AddNewPhotoToListingHandler {

    private final Hosts hosts;
    private final MakeNewListingPhoto makeNewListingPhoto;

    @Override
    public void handle(AddNewPhotoToListing command)
        throws Exception.ListingNotFound, Exception.PhotoNotFound,
        Exception.AccessForbidden, Exception.PhotoAlreadyExists {

    }
}
