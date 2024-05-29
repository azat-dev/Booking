package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlResponseBody;
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
 * API tests for CommandsUpdateUserPhotoApi
 */
class CommandsUpdateUserPhotoApiTest {

    private CommandsUpdateUserPhotoApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(CommandsUpdateUserPhotoApi.class);
    }

    
    /**
     * Generate upload form for user photo
     *
     * 
     */
    @Test
    void generateUploadUserPhotoUrlTest() {
        GenerateUploadUserPhotoUrlRequestBody generateUploadUserPhotoUrlRequestBody = null;
        // GenerateUploadUserPhotoUrlResponseBody response = api.generateUploadUserPhotoUrl(generateUploadUserPhotoUrlRequestBody);

        // TODO: test validations
    }

    
    /**
     * Attach uploaded photo to the user
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
