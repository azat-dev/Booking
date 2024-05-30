package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.usersms.generated.client.model.ApiResponse;

import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmailRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmailResponseBody;
import com.azat4dev.booking.usersms.generated.client.model.UserWithSameEmailAlreadyExistsErrorDTO;
import com.azat4dev.booking.usersms.generated.client.model.VerifyEmail400Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-30T09:25:21.113148+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface CommandsSignUpApi extends ApiClient.Api {


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


}
