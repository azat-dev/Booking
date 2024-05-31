package com.azat4dev.booking.listingsms.generated.client.api;

import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.listingsms.generated.client.model.ApiResponse;

import com.azat4dev.booking.listingsms.generated.client.model.AddListing401Response;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingResponse;
import com.azat4dev.booking.listingsms.generated.client.model.ErrorDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-06-01T00:08:22.906873+03:00[Europe/Moscow]")
public interface CommandsModificationsApi extends ApiClient.Api {


  /**
   * Add a new listing
   * 
   * @param addListingRequestBody  (required)
   * @return AddListingResponse
   */
  @RequestLine("POST /api/private/listings")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  AddListingResponse addListing(AddListingRequestBody addListingRequestBody);

  /**
   * Add a new listing
   * Similar to <code>addListing</code> but it also returns the http response headers .
   * 
   * @param addListingRequestBody  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/private/listings")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<AddListingResponse> addListingWithHttpInfo(AddListingRequestBody addListingRequestBody);


}
