package com.azat4dev.booking.listingsms.queries.presentation.api.mappers;

import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;

public interface GetListingPhotoUrl {

    String execute(MediaObjectName objectName);
}
