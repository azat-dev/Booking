package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlResponseBody;
import com.azat4dev.booking.usersms.generated.client.model.PersonalUserInfoDTO;
import com.azat4dev.booking.usersms.generated.client.model.UpdateUserPhotoRequestBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CurrentUserApi
 */
class CurrentUserApiTest {

    private CurrentUserApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(CurrentUserApi.class);
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
     * Gets current user info
     *
     * 
     */
    @Test
    void getCurrentUserTest() {
        // PersonalUserInfoDTO response = api.getCurrentUser();

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

    
}
