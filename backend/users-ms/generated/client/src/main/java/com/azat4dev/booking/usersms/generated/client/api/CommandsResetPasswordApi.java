package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.usersms.generated.client.model.ApiResponse;

import com.azat4dev.booking.usersms.generated.client.model.CompleteResetPasswordRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.ResetPasswordByEmailRequestBody;
import com.azat4dev.booking.usersms.generated.client.model.VerifyEmail400Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-30T01:15:08.126842+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface CommandsResetPasswordApi extends ApiClient.Api {


  /**
   * Reset password by email
   * 
   * @param completeResetPasswordRequestBody JSON payload (required)
   * @return String
   */
  @RequestLine("POST /api/public/password/set-new")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  String completeResetPassword(CompleteResetPasswordRequestBody completeResetPasswordRequestBody);

  /**
   * Reset password by email
   * Similar to <code>completeResetPassword</code> but it also returns the http response headers .
   * 
   * @param completeResetPasswordRequestBody JSON payload (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/public/password/set-new")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<String> completeResetPasswordWithHttpInfo(CompleteResetPasswordRequestBody completeResetPasswordRequestBody);



  /**
   * Reset password by email
   * 
   * @param resetPasswordByEmailRequestBody JSON payload (required)
   * @return String
   */
  @RequestLine("POST /api/public/password/reset")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  String resetPasswordByEmail(ResetPasswordByEmailRequestBody resetPasswordByEmailRequestBody);

  /**
   * Reset password by email
   * Similar to <code>resetPasswordByEmail</code> but it also returns the http response headers .
   * 
   * @param resetPasswordByEmailRequestBody JSON payload (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/public/password/reset")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<String> resetPasswordByEmailWithHttpInfo(ResetPasswordByEmailRequestBody resetPasswordByEmailRequestBody);


}
