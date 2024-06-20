package com.azat4dev.booking.listingsms.queries.presentation.api.mappers;

import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;

public interface GetListingPhotoUrl {

    String execute(BucketName bucketName, MediaObjectName objectName);
}
