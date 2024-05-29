package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.usersms.generated.client.model.ApiResponse;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-29T10:57:01.921409+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface CommandsApi extends ApiClient.Api {


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
   * Get a new pair of tokens (access, refresh)
   * 
   * @param authenticateByEmailRequestBody JSON payload (required)
   * @return GetTokenResponseBody
   */
  @RequestLine("POST /api/public/auth/token")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  GetTokenResponseBody getNewTokensByEmail(AuthenticateByEmailRequestBody authenticateByEmailRequestBody);

  /**
   * Get a new pair of tokens (access, refresh)
   * Similar to <code>getNewTokensByEmail</code> but it also returns the http response headers .
   * 
   * @param authenticateByEmailRequestBody JSON payload (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/public/auth/token")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<GetTokenResponseBody> getNewTokensByEmailWithHttpInfo(AuthenticateByEmailRequestBody authenticateByEmailRequestBody);



  /**
   * Reset password by email
   * 
   * @param resetPasswordByEmailRequestBody JSON payload (required)
   */
  @RequestLine("POST /api/public/reset-password")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  void resetPasswordByEmail(ResetPasswordByEmailRequestBody resetPasswordByEmailRequestBody);

  /**
   * Reset password by email
   * Similar to <code>resetPasswordByEmail</code> but it also returns the http response headers .
   * 
   * @param resetPasswordByEmailRequestBody JSON payload (required)
   */
  @RequestLine("POST /api/public/reset-password")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<Void> resetPasswordByEmailWithHttpInfo(ResetPasswordByEmailRequestBody resetPasswordByEmailRequestBody);



  /**
   * Sign up a new user
   * 
   * @param signUpByEmailRequestBody JSON payload (required)
   * @return SignUpByEmailResponseBody
   */
  @RequestLine("POST /api/public/auth/sign-up")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  SignUpByEmailResponseBody signUpByEmail(SignUpByEmailRequestBody signUpByEmailRequestBody);

  /**
   * Sign up a new user
   * Similar to <code>signUpByEmail</code> but it also returns the http response headers .
   * 
   * @param signUpByEmailRequestBody JSON payload (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/public/auth/sign-up")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<SignUpByEmailResponseBody> signUpByEmailWithHttpInfo(SignUpByEmailRequestBody signUpByEmailRequestBody);



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



  /**
   * Check if a token is valid
   * 
   * @param verifyTokenRequest JSON payload (required)
   */
  @RequestLine("POST /api/public/auth/token/verify")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  void verifyToken(VerifyTokenRequest verifyTokenRequest);

  /**
   * Check if a token is valid
   * Similar to <code>verifyToken</code> but it also returns the http response headers .
   * 
   * @param verifyTokenRequest JSON payload (required)
   */
  @RequestLine("POST /api/public/auth/token/verify")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<Void> verifyTokenWithHttpInfo(VerifyTokenRequest verifyTokenRequest);


}
