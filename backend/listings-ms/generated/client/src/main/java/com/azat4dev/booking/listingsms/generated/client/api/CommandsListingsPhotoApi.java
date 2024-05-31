package com.azat4dev.booking.listingsms.generated.client.api;

import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.base.EncodingUtils;
import com.azat4dev.booking.listingsms.generated.client.model.ApiResponse;

import com.azat4dev.booking.listingsms.generated.client.model.AddListingPhotoRequestBody;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingPhotoResponseBody;
import com.azat4dev.booking.listingsms.generated.client.model.GenerateUploadListingPhotoUrlRequestBody;
import com.azat4dev.booking.listingsms.generated.client.model.GenerateUploadListingPhotoUrlResponseBody;
import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-06-01T00:08:22.906873+03:00[Europe/Moscow]")
public interface CommandsListingsPhotoApi extends ApiClient.Api {


  /**
   * Add photo to a listing
   * 
   * @param listingId Listing Id (required)
   * @param addListingPhotoRequestBody  (required)
   * @return AddListingPhotoResponseBody
   */
  @RequestLine("POST /api/private/listings/{listingId}/photos/add")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  AddListingPhotoResponseBody addPhotoToListing(@Param("listingId") UUID listingId, AddListingPhotoRequestBody addListingPhotoRequestBody);

  /**
   * Add photo to a listing
   * Similar to <code>addPhotoToListing</code> but it also returns the http response headers .
   * 
   * @param listingId Listing Id (required)
   * @param addListingPhotoRequestBody  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/private/listings/{listingId}/photos/add")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<AddListingPhotoResponseBody> addPhotoToListingWithHttpInfo(@Param("listingId") UUID listingId, AddListingPhotoRequestBody addListingPhotoRequestBody);



  /**
   * Delete photo from a listing
   * 
   * @param listingId Listing Id (required)
   * @param photoId Listing Id (required)
   * @return AddListingPhotoResponseBody
   */
  @RequestLine("DELETE /api/private/listings/{listingId}/photos/{photoId}")
  @Headers({
    "Accept: application/json",
  })
  AddListingPhotoResponseBody deletePhoto(@Param("listingId") UUID listingId, @Param("photoId") UUID photoId);

  /**
   * Delete photo from a listing
   * Similar to <code>deletePhoto</code> but it also returns the http response headers .
   * 
   * @param listingId Listing Id (required)
   * @param photoId Listing Id (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("DELETE /api/private/listings/{listingId}/photos/{photoId}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<AddListingPhotoResponseBody> deletePhotoWithHttpInfo(@Param("listingId") UUID listingId, @Param("photoId") UUID photoId);



  /**
   * Generate upload form for listing photo
   * 
   * @param listingId Listing Id (required)
   * @param generateUploadListingPhotoUrlRequestBody  (required)
   * @return GenerateUploadListingPhotoUrlResponseBody
   */
  @RequestLine("POST /api/private/listings/{listingId}/photos/get-upload-url")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  GenerateUploadListingPhotoUrlResponseBody generateUploadListingPhotoUrl(@Param("listingId") UUID listingId, GenerateUploadListingPhotoUrlRequestBody generateUploadListingPhotoUrlRequestBody);

  /**
   * Generate upload form for listing photo
   * Similar to <code>generateUploadListingPhotoUrl</code> but it also returns the http response headers .
   * 
   * @param listingId Listing Id (required)
   * @param generateUploadListingPhotoUrlRequestBody  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/private/listings/{listingId}/photos/get-upload-url")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<GenerateUploadListingPhotoUrlResponseBody> generateUploadListingPhotoUrlWithHttpInfo(@Param("listingId") UUID listingId, GenerateUploadListingPhotoUrlRequestBody generateUploadListingPhotoUrlRequestBody);


}
