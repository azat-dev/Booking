package com.azat4dev.booking.listingsms.queries.presentation.api.mappers;

import com.azat4dev.booking.shared.domain.values.BaseUrl;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import com.azat4dev.booking.shared.domain.values.files.MediaObjectName;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class GetListingPhotoUrlImpl implements GetListingPhotoUrl {

    private final BaseUrl baseUrl;

    @Override
    public String execute(BucketName bucketName, MediaObjectName objectName) {
        return baseUrl + bucketName.getValue() + "/" + objectName.getValue();
    }
}
