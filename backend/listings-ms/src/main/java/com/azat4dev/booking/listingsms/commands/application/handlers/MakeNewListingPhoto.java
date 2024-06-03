package com.azat4dev.booking.listingsms.commands.application.handlers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.shared.domain.values.files.UploadedFileData;

@FunctionalInterface
public interface MakeNewListingPhoto {

    ListingPhoto execute(UploadedFileData uploadedFileData);
}
