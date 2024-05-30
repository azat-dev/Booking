package com.azat4dev.booking.listingsms.generated.client.api;

import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.model.GetListingPrivateDetailsResponse;
import com.azat4dev.booking.listingsms.generated.client.model.ListingPrivateDetailsDTO;
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
 * API tests for QueriesPrivateApi
 */
class QueriesPrivateApiTest {

    private QueriesPrivateApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(QueriesPrivateApi.class);
    }

    
    /**
     * Get private details for listing
     *
     * 
     */
    @Test
    void getListingPrivateDetailsTest() {
        UUID listingId = null;
        // GetListingPrivateDetailsResponse response = api.getListingPrivateDetails(listingId);

        // TODO: test validations
    }

    
    /**
     * List own listings
     *
     * 
     */
    @Test
    void getOwnListingsTest() {
        // List<ListingPrivateDetailsDTO> response = api.getOwnListings();

        // TODO: test validations
    }

    
}
