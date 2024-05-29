package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.model.ResetPasswordByEmailRequestBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for PasswordApi
 */
class PasswordApiTest {

    private PasswordApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(PasswordApi.class);
    }

    
    /**
     * Reset password by email
     *
     * 
     */
    @Test
    void resetPasswordByEmailTest() {
        ResetPasswordByEmailRequestBody resetPasswordByEmailRequestBody = null;
        // api.resetPasswordByEmail(resetPasswordByEmailRequestBody);

        // TODO: test validations
    }

    
}
