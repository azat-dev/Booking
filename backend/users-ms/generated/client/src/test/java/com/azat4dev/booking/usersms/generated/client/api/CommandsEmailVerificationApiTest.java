package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.model.VerifyEmail200Response;
import com.azat4dev.booking.usersms.generated.client.model.VerifyEmail400Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CommandsEmailVerificationApi
 */
class CommandsEmailVerificationApiTest {

    private CommandsEmailVerificationApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(CommandsEmailVerificationApi.class);
    }

    
    /**
     * Send email for verification
     *
     * 
     */
    @Test
    void verifyEmailTest() {
        String token = null;
        // VerifyEmail200Response response = api.verifyEmail(token);

        // TODO: test validations
    }

    
}
