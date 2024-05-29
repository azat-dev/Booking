package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.usersms.generated.client.model.ApiResponse;

import com.azat4dev.booking.usersms.generated.client.model.LoginByEmailRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.LoginByEmailResponseBody;
import com.azat4dev.booking.usersms.generated.client.model.VerifyEmail400Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-30T01:15:08.126842+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface CommandsLoginApi extends ApiClient.Api {


  /**
   * Get a new pair of tokens by email (access, refresh)
   * 
   * @param loginByEmailRequestBody JSON payload (required)
   * @return LoginByEmailResponseBody
   */
  @RequestLine("POST /api/public/auth/login")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  LoginByEmailResponseBody loginByEmail(LoginByEmailRequestBody loginByEmailRequestBody);

  /**
   * Get a new pair of tokens by email (access, refresh)
   * Similar to <code>loginByEmail</code> but it also returns the http response headers .
   * 
   * @param loginByEmailRequestBody JSON payload (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/public/auth/login")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<LoginByEmailResponseBody> loginByEmailWithHttpInfo(LoginByEmailRequestBody loginByEmailRequestBody);


}
