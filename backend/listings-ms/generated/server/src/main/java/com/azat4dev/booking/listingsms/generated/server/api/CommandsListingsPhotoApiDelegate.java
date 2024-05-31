package com.azat4dev.booking.listingsms.generated.server.api;

import com.azat4dev.booking.listingsms.generated.server.model.AddListingPhotoRequestBody;
import com.azat4dev.booking.listingsms.generated.server.model.AddListingPhotoResponseBody;
import com.azat4dev.booking.listingsms.generated.server.model.GenerateUploadListingPhotoUrlRequestBody;
import com.azat4dev.booking.listingsms.generated.server.model.GenerateUploadListingPhotoUrlResponseBody;
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
 * A delegate to be called by the {@link CommandsListingsPhotoApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-01T00:08:26.456416+03:00[Europe/Moscow]")
public interface CommandsListingsPhotoApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /api/private/listings/{listingId}/photos/add : Add photo to a listing
     *
     * @param listingId Listing Id (required)
     * @param addListingPhotoRequestBody  (required)
     * @return Photo attached (status code 200)
     *         or User is not authorized (status code 403)
     * @see CommandsListingsPhotoApi#addPhotoToListing
     */
    default ResponseEntity<AddListingPhotoResponseBody> addPhotoToListing(UUID listingId,
        AddListingPhotoRequestBody addListingPhotoRequestBody) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"listingPhotoId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"operationId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * DELETE /api/private/listings/{listingId}/photos/{photoId} : Delete photo from a listing
     *
     * @param listingId Listing Id (required)
     * @param photoId Listing Id (required)
     * @return Photo attached (status code 204)
     *         or User is not authorized (status code 403)
     * @see CommandsListingsPhotoApi#deletePhoto
     */
    default ResponseEntity<AddListingPhotoResponseBody> deletePhoto(UUID listingId,
        UUID photoId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"listingPhotoId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"operationId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * POST /api/private/listings/{listingId}/photos/get-upload-url : Generate upload form for listing photo
     *
     * @param listingId Listing Id (required)
     * @param generateUploadListingPhotoUrlRequestBody  (required)
     * @return OK (status code 200)
     * @see CommandsListingsPhotoApi#generateUploadListingPhotoUrl
     */
    default ResponseEntity<GenerateUploadListingPhotoUrlResponseBody> generateUploadListingPhotoUrl(UUID listingId,
        GenerateUploadListingPhotoUrlRequestBody generateUploadListingPhotoUrlRequestBody) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"objectPath\" : { \"bucketName\" : \"bucketName\", \"objectName\" : \"objectName\", \"url\" : \"url\" }, \"formData\" : { \"key\" : \"formData\" } }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
