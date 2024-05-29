package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.usersms.generated.client.model.ApiResponse;

import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlResponseBody;
import com.azat4dev.booking.usersms.generated.client.model.PersonalUserInfoDTO;
import com.azat4dev.booking.usersms.generated.client.model.UpdateUserPhotoRequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-29T10:57:01.921409+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface CurrentUserApi extends ApiClient.Api {


  /**
   * POST api/with-auth/users/current/get-upload-url-user-photo
   * 
   * @param generateUploadUserPhotoUrlRequestBody  (required)
   * @return GenerateUploadUserPhotoUrlResponseBody
   */
  @RequestLine("POST /api/with-auth/users/current/get-upload-url-user-photo")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  GenerateUploadUserPhotoUrlResponseBody generateUploadUserPhototUrl(GenerateUploadUserPhotoUrlRequestBody generateUploadUserPhotoUrlRequestBody);

  /**
   * POST api/with-auth/users/current/get-upload-url-user-photo
   * Similar to <code>generateUploadUserPhototUrl</code> but it also returns the http response headers .
   * 
   * @param generateUploadUserPhotoUrlRequestBody  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/with-auth/users/current/get-upload-url-user-photo")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<GenerateUploadUserPhotoUrlResponseBody> generateUploadUserPhototUrlWithHttpInfo(GenerateUploadUserPhotoUrlRequestBody generateUploadUserPhotoUrlRequestBody);



  /**
   * Gets current user info
   * 
   * @return PersonalUserInfoDTO
   */
  @RequestLine("GET /api/with-auth/users/current")
  @Headers({
    "Accept: application/json",
  })
  PersonalUserInfoDTO getCurrentUser();

  /**
   * Gets current user info
   * Similar to <code>getCurrentUser</code> but it also returns the http response headers .
   * 
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/with-auth/users/current")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<PersonalUserInfoDTO> getCurrentUserWithHttpInfo();



  /**
   * POST api/with-auth/users/current/update-photo
   * 
   * @param updateUserPhotoRequestBody  (required)
   * @return String
   */
  @RequestLine("POST /api/with-auth/users/current/update-photo")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  String updateUserPhoto(UpdateUserPhotoRequestBody updateUserPhotoRequestBody);

  /**
   * POST api/with-auth/users/current/update-photo
   * Similar to <code>updateUserPhoto</code> but it also returns the http response headers .
   * 
   * @param updateUserPhotoRequestBody  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/with-auth/users/current/update-photo")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<String> updateUserPhotoWithHttpInfo(UpdateUserPhotoRequestBody updateUserPhotoRequestBody);


}
