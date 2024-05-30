package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.usersms.generated.client.model.ApiResponse;

import com.azat4dev.booking.usersms.generated.client.model.PersonalUserInfoDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-30T09:25:21.113148+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface QueriesCurrentUserApi extends ApiClient.Api {


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


}
