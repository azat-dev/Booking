package com.azat4dev.booking.listingsms.generated.client.api;

import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.model.AddListing401Response;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingResponse;
import com.azat4dev.booking.listingsms.generated.client.model.ErrorDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CommandsModificationsApi
 */
class CommandsModificationsApiTest {

    private CommandsModificationsApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(CommandsModificationsApi.class);
    }

    
    /**
     * Add a new listing
     *
     * 
     */
    @Test
    void addListingTest() {
        AddListingRequestBody addListingRequestBody = null;
        // AddListingResponse response = api.addListing(addListingRequestBody);

        // TODO: test validations
    }

    
}
