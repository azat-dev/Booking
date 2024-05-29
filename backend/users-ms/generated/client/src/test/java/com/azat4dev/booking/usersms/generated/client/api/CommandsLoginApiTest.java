package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.model.LoginByEmailRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.LoginByEmailResponseBody;
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
 * API tests for CommandsLoginApi
 */
class CommandsLoginApiTest {

    private CommandsLoginApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(CommandsLoginApi.class);
    }

    
    /**
     * Get a new pair of tokens by email (access, refresh)
     *
     * 
     */
    @Test
    void loginByEmailTest() {
        LoginByEmailRequestBody loginByEmailRequestBody = null;
        // LoginByEmailResponseBody response = api.loginByEmail(loginByEmailRequestBody);

        // TODO: test validations
    }

    
}
