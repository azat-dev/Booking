package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.model.AuthenticateByEmailRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlResponseBody;
import com.azat4dev.booking.usersms.generated.client.model.GetTokenResponseBody;
import com.azat4dev.booking.usersms.generated.client.model.ResetPasswordByEmailRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmail400Response;
import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmailRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmailResponseBody;
import com.azat4dev.booking.usersms.generated.client.model.UpdateUserPhotoRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.UserWithSameEmailAlreadyExistsErrorDTO;
import com.azat4dev.booking.usersms.generated.client.model.VerifyTokenRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CommandsApi
 */
class CommandsApiTest {

    private CommandsApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(CommandsApi.class);
    }

    
    /**
     * POST api/with-auth/users/current/get-upload-url-user-photo
     *
     * 
     */
    @Test
    void generateUploadUserPhototUrlTest() {
        GenerateUploadUserPhotoUrlRequestBody generateUploadUserPhotoUrlRequestBody = null;
        // GenerateUploadUserPhotoUrlResponseBody response = api.generateUploadUserPhototUrl(generateUploadUserPhotoUrlRequestBody);

        // TODO: test validations
    }

    
    /**
     * Get a new pair of tokens (access, refresh)
     *
     * 
     */
    @Test
    void getNewTokensByEmailTest() {
        AuthenticateByEmailRequestBody authenticateByEmailRequestBody = null;
        // GetTokenResponseBody response = api.getNewTokensByEmail(authenticateByEmailRequestBody);

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
        // api.resetPasswordByEmail(resetPasswordByEmailRequestBody);

        // TODO: test validations
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

    
    /**
     * POST api/with-auth/users/current/update-photo
     *
     * 
     */
    @Test
    void updateUserPhotoTest() {
        UpdateUserPhotoRequestBody updateUserPhotoRequestBody = null;
        // String response = api.updateUserPhoto(updateUserPhotoRequestBody);

        // TODO: test validations
    }

    
    /**
     * Check if a token is valid
     *
     * 
     */
    @Test
    void verifyTokenTest() {
        VerifyTokenRequest verifyTokenRequest = null;
        // api.verifyToken(verifyTokenRequest);

        // TODO: test validations
    }

    
}
