package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.usersms.generated.client.model.ApiResponse;

import com.azat4dev.booking.usersms.generated.client.model.ResetPasswordByEmailRequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-29T10:57:01.921409+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface PasswordApi extends ApiClient.Api {


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


}
