package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.usersms.generated.client.model.ApiResponse;

import com.azat4dev.booking.usersms.generated.client.model.VerifyEmail200Response;
import com.azat4dev.booking.usersms.generated.client.model.VerifyEmail400Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-30T01:15:08.126842+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface CommandsEmailVerificationApi extends ApiClient.Api {


  /**
   * Send email for verification
   * 
   * @param token  (required)
   * @return VerifyEmail200Response
   */
  @RequestLine("GET /api/public/verify-email")
  @Headers({
    "Accept: application/json",
  })
  VerifyEmail200Response verifyEmail(@Param("token") String token);

  /**
   * Send email for verification
   * Similar to <code>verifyEmail</code> but it also returns the http response headers .
   * 
   * @param token  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/public/verify-email")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<VerifyEmail200Response> verifyEmailWithHttpInfo(@Param("token") String token);


}
