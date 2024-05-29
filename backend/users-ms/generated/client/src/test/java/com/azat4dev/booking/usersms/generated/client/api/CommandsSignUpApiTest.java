package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmailRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmailResponseBody;
import com.azat4dev.booking.usersms.generated.client.model.UserWithSameEmailAlreadyExistsErrorDTO;
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
 * API tests for CommandsSignUpApi
 */
class CommandsSignUpApiTest {

    private CommandsSignUpApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(CommandsSignUpApi.class);
    }

    
    /**
     * Sign up a new user
     *
     * 
     */
    @Test
    void signUpByEmailTest() {
        SignUpByEmailRequestBody signUpByEmailRequestBody = null;
        // SignUpByEmailResponseBody response = api.signUpByEmail(signUpByEmailRequestBody);

        // TODO: test validations
    }

    
}
