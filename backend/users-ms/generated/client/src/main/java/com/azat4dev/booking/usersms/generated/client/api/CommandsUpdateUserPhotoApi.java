package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.usersms.generated.client.model.ApiResponse;

import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlResponseBody;
import com.azat4dev.booking.usersms.generated.client.model.UpdateUserPhoto200Response;
import com.azat4dev.booking.usersms.generated.client.model.UpdateUserPhotoRequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-30T09:25:21.113148+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface CommandsUpdateUserPhotoApi extends ApiClient.Api {


  /**
   * Generate upload form for user photo
   * 
   * @param generateUploadUserPhotoUrlRequestBody  (required)
   * @return GenerateUploadUserPhotoUrlResponseBody
   */
  @RequestLine("POST /api/with-auth/users/current/photo/get-upload-url")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  GenerateUploadUserPhotoUrlResponseBody generateUploadUserPhotoUrl(GenerateUploadUserPhotoUrlRequestBody generateUploadUserPhotoUrlRequestBody);

  /**
   * Generate upload form for user photo
   * Similar to <code>generateUploadUserPhotoUrl</code> but it also returns the http response headers .
   * 
   * @param generateUploadUserPhotoUrlRequestBody  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/with-auth/users/current/photo/get-upload-url")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<GenerateUploadUserPhotoUrlResponseBody> generateUploadUserPhotoUrlWithHttpInfo(GenerateUploadUserPhotoUrlRequestBody generateUploadUserPhotoUrlRequestBody);



  /**
   * Attach uploaded photo to the user
   * 
   * @param updateUserPhotoRequestBody  (required)
   * @return UpdateUserPhoto200Response
   */
  @RequestLine("POST /api/with-auth/users/current/photo/update")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  UpdateUserPhoto200Response updateUserPhoto(UpdateUserPhotoRequestBody updateUserPhotoRequestBody);

  /**
   * Attach uploaded photo to the user
   * Similar to <code>updateUserPhoto</code> but it also returns the http response headers .
   * 
   * @param updateUserPhotoRequestBody  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/with-auth/users/current/photo/update")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<UpdateUserPhoto200Response> updateUserPhotoWithHttpInfo(UpdateUserPhotoRequestBody updateUserPhotoRequestBody);


}
