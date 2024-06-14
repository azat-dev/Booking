package com.azat4dev.booking.usersms.generated.server.api;

import com.azat4dev.booking.usersms.generated.server.model.GenerateUploadUserPhotoUrlRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.GenerateUploadUserPhotoUrlResponseBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.UpdateUserPhoto200ResponseDTO;
import com.azat4dev.booking.usersms.generated.server.model.UpdateUserPhotoRequestBodyDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

/**
 * A delegate to be called by the {@link CommandsUpdateUserPhotoApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public interface CommandsUpdateUserPhotoApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /api/private/identity/users/current/photo/get-upload-url : Generate upload form for user photo
     *
     * @param generateUploadUserPhotoUrlRequestBodyDTO  (required)
     * @return OK (status code 200)
     * @see CommandsUpdateUserPhotoApi#generateUploadUserPhotoUrl
     */
    default ResponseEntity<GenerateUploadUserPhotoUrlResponseBodyDTO> generateUploadUserPhotoUrl(GenerateUploadUserPhotoUrlRequestBodyDTO generateUploadUserPhotoUrlRequestBodyDTO) throws Exception {
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

    /**
     * POST /api/private/identity/users/current/photo/update : Attach uploaded photo to the user
     *
     * @param updateUserPhotoRequestBodyDTO  (required)
     * @return Photo updated (status code 200)
     * @see CommandsUpdateUserPhotoApi#updateUserPhoto
     */
    default ResponseEntity<UpdateUserPhoto200ResponseDTO> updateUserPhoto(UpdateUserPhotoRequestBodyDTO updateUserPhotoRequestBodyDTO) throws Exception {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Photo updated\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
