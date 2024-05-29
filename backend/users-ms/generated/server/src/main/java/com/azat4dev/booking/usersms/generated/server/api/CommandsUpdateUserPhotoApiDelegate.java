package com.azat4dev.booking.usersms.generated.server.api;

import com.azat4dev.booking.usersms.generated.server.model.GenerateUploadUserPhotoUrlRequestBody;
import com.azat4dev.booking.usersms.generated.server.model.GenerateUploadUserPhotoUrlResponseBody;
import com.azat4dev.booking.usersms.generated.server.model.UpdateUserPhotoRequestBody;
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
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-30T01:15:09.334912+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface CommandsUpdateUserPhotoApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /api/with-auth/users/current/photo/get-upload-url : Generate upload form for user photo
     *
     * @param generateUploadUserPhotoUrlRequestBody  (required)
     * @return OK (status code 200)
     * @see CommandsUpdateUserPhotoApi#generateUploadUserPhotoUrl
     */
    default ResponseEntity<GenerateUploadUserPhotoUrlResponseBody> generateUploadUserPhotoUrl(GenerateUploadUserPhotoUrlRequestBody generateUploadUserPhotoUrlRequestBody) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("*/*"))) {
                    String exampleString = "{ \"objectPath\" : { \"bucketName\" : \"bucketName\", \"objectName\" : \"objectName\", \"url\" : \"url\" }, \"formData\" : { \"key\" : \"formData\" } }";
                    ApiUtil.setExampleResponse(request, "*/*", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * POST /api/with-auth/users/current/photo/update : Attach uploaded photo to the user
     *
     * @param updateUserPhotoRequestBody  (required)
     * @return Photo updated (status code 200)
     * @see CommandsUpdateUserPhotoApi#updateUserPhoto
     */
    default ResponseEntity<String> updateUserPhoto(UpdateUserPhotoRequestBody updateUserPhotoRequestBody) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
