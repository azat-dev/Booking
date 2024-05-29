package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.usersms.generated.client.model.ApiResponse;

import com.azat4dev.booking.usersms.generated.client.model.AuthenticateByEmailRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.GetTokenResponseBody;
import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmail400Response;
import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmailRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmailResponseBody;
import com.azat4dev.booking.usersms.generated.client.model.UserWithSameEmailAlreadyExistsErrorDTO;
import com.azat4dev.booking.usersms.generated.client.model.VerifyTokenRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-29T10:57:01.921409+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface AuthApi extends ApiClient.Api {


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
