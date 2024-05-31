package com.azat4dev.booking.listingsms.generated.client.api;

import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingPhotoRequestBody;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingPhotoResponseBody;
import com.azat4dev.booking.listingsms.generated.client.model.GenerateUploadListingPhotoUrlRequestBody;
import com.azat4dev.booking.listingsms.generated.client.model.GenerateUploadListingPhotoUrlResponseBody;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CommandsListingsPhotoApi
 */
class CommandsListingsPhotoApiTest {

    private CommandsListingsPhotoApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(CommandsListingsPhotoApi.class);
    }

    
    /**
     * Add photo to a listing
     *
     * 
     */
    @Test
    void addPhotoToListingTest() {
        UUID listingId = null;
        AddListingPhotoRequestBody addListingPhotoRequestBody = null;
        // AddListingPhotoResponseBody response = api.addPhotoToListing(listingId, addListingPhotoRequestBody);

        // TODO: test validations
    }

    
    /**
     * Delete photo from a listing
     *
     * 
     */
    @Test
    void deletePhotoTest() {
        UUID listingId = null;
        UUID photoId = null;
        // AddListingPhotoResponseBody response = api.deletePhoto(listingId, photoId);

        // TODO: test validations
    }

    
    /**
     * Generate upload form for listing photo
     *
     * 
     */
    @Test
    void generateUploadListingPhotoUrlTest() {
        UUID listingId = null;
        GenerateUploadListingPhotoUrlRequestBody generateUploadListingPhotoUrlRequestBody = null;
        // GenerateUploadListingPhotoUrlResponseBody response = api.generateUploadListingPhotoUrl(listingId, generateUploadListingPhotoUrlRequestBody);

        // TODO: test validations
    }

    
}
