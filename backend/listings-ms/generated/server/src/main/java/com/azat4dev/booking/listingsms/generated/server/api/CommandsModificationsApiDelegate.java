package com.azat4dev.booking.listingsms.generated.server.api;

import com.azat4dev.booking.listingsms.generated.server.model.AddListing401Response;
import com.azat4dev.booking.listingsms.generated.server.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.generated.server.model.AddListingResponse;
import com.azat4dev.booking.listingsms.generated.server.model.ErrorDTO;
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
 * A delegate to be called by the {@link CommandsModificationsApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-30T14:24:06.893700+03:00[Europe/Moscow]")
public interface CommandsModificationsApiDelegate {

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
     * @see CommandsModificationsApi#addListing
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

}
