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

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-30T09:25:21.113148+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface CommandsEmailVerificationApi extends ApiClient.Api {


  /**
   * Send email for verification
   * 
   * @param token  (required)
   * @return VerifyEmail200Response
   */
  @RequestLine("GET /api/public/verify-email?token={token}")
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
  @RequestLine("GET /api/public/verify-email?token={token}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<VerifyEmail200Response> verifyEmailWithHttpInfo(@Param("token") String token);


  /**
   * Send email for verification
   * 
   * Note, this is equivalent to the other <code>verifyEmail</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link VerifyEmailQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>token -  (required)</li>
   *   </ul>
   * @return VerifyEmail200Response
   */
  @RequestLine("GET /api/public/verify-email?token={token}")
  @Headers({
  "Accept: application/json",
  })
  VerifyEmail200Response verifyEmail(@QueryMap(encoded=true) VerifyEmailQueryParams queryParams);

  /**
  * Send email for verification
  * 
  * Note, this is equivalent to the other <code>verifyEmail</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>token -  (required)</li>
      *   </ul>
          * @return VerifyEmail200Response
      */
      @RequestLine("GET /api/public/verify-email?token={token}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<VerifyEmail200Response> verifyEmailWithHttpInfo(@QueryMap(encoded=true) VerifyEmailQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>verifyEmail</code> method in a fluent style.
   */
  public static class VerifyEmailQueryParams extends HashMap<String, Object> {
    public VerifyEmailQueryParams token(final String value) {
      put("token", EncodingUtils.encode(value));
      return this;
    }
  }
}
