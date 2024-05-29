package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.model.CompleteResetPasswordRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.ResetPasswordByEmailRequestBody;
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
 * API tests for CommandsResetPasswordApi
 */
class CommandsResetPasswordApiTest {

    private CommandsResetPasswordApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(CommandsResetPasswordApi.class);
    }

    
    /**
     * Reset password by email
     *
     * 
     */
    @Test
    void completeResetPasswordTest() {
        CompleteResetPasswordRequestBody completeResetPasswordRequestBody = null;
        // String response = api.completeResetPassword(completeResetPasswordRequestBody);

        // TODO: test validations
    }

    
    /**
     * Reset password by email
     *
     * 
     */
    @Test
    void resetPasswordByEmailTest() {
        ResetPasswordByEmailRequestBody resetPasswordByEmailRequestBody = null;
        // String response = api.resetPasswordByEmail(resetPasswordByEmailRequestBody);

        // TODO: test validations
    }

    
}
