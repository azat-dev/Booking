package com.azat4dev.booking.listingsms.generated.server.api;

import com.azat4dev.booking.listingsms.generated.server.model.GetListingPrivateDetailsResponse;
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
 * A delegate to be called by the {@link QueriesPrivateApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-30T20:48:06.837109+03:00[Europe/Moscow]")
public interface QueriesPrivateApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /api/private/users/currrent/listings/{listingId} : Get private details for listing
     *
     * @param listingId Listing ID (required)
     * @return Private details of listing (status code 200)
     *         or Not valid Token (status code 401)
     *         or Not found (status code 404)
     * @see QueriesPrivateApi#getListingPrivateDetails
     */
    default ResponseEntity<GetListingPrivateDetailsResponse> getListingPrivateDetails(UUID listingId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"listing\" : { \"description\" : \"description\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"title\" : \"title\" } }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
