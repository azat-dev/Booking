package com.azat4dev.booking.listingsms.generated.server.api;

import com.azat4dev.booking.listingsms.generated.server.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.generated.server.model.AddListingResponse;
import com.azat4dev.booking.listingsms.generated.server.model.GetPhotoUploadUrlForListingResponse;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

/**
 * A delegate to be called by the {@link CommandsApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-28T23:07:12.290925+03:00[Europe/Moscow]")
public interface CommandsApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /api/private/listings : Add a new listing
     *
     * @param addListingRequestBody  (required)
     * @return Listing added successfully (status code 200)
     *         or Not valid Token (status code 401)
     *         or Not found (status code 404)
     * @see CommandsApi#addListing
     */
    default ResponseEntity<AddListingResponse> addListing(AddListingRequestBody addListingRequestBody) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"listingId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * POST /api/private/listings/{listingId}/get-photo-upload-url : Get upload URL for listing photo
     *
     * @param listingId Listing ID (required)
     * @param addListingRequestBody  (required)
     * @return Listing added successfully (status code 200)
     *         or Not valid Token (status code 401)
     *         or Not found (status code 404)
     * @see CommandsApi#getPhotoUploadUrl
     */
    default ResponseEntity<GetPhotoUploadUrlForListingResponse> getPhotoUploadUrl(UUID listingId,
        AddListingRequestBody addListingRequestBody) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"url\" : \"https://openapi-generator.tech\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
