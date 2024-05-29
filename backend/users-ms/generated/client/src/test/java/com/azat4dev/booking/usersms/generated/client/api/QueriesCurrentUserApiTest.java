package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.model.PersonalUserInfoDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for QueriesCurrentUserApi
 */
class QueriesCurrentUserApiTest {

    private QueriesCurrentUserApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(QueriesCurrentUserApi.class);
    }

    
    /**
     * Gets current user info
     *
     * 
     */
    @Test
    void getCurrentUserTest() {
        // PersonalUserInfoDTO response = api.getCurrentUser();

        // TODO: test validations
    }

    
}
