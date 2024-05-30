package com.azat4dev.booking.usersms.generated.server.api;

import com.azat4dev.booking.usersms.generated.server.model.LoginByEmailRequestBody;
import com.azat4dev.booking.usersms.generated.server.model.LoginByEmailResponseBody;
import com.azat4dev.booking.usersms.generated.server.model.VerifyEmail400Response;
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
 * A delegate to be called by the {@link CommandsLoginApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-05-30T09:25:21.695569+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public interface CommandsLoginApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /api/public/auth/login : Get a new pair of tokens by email (access, refresh)
     *
     * @param loginByEmailRequestBody JSON payload (required)
     * @return Success (status code 200)
     *         or User not found (status code 403)
     * @see CommandsLoginApi#loginByEmail
     */
    default ResponseEntity<LoginByEmailResponseBody> loginByEmail(LoginByEmailRequestBody loginByEmailRequestBody) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"tokens\" : { \"access\" : \"access\", \"refresh\" : \"refresh\" }, \"userId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"type\" : \"validationError\", \"errors\" : [ { \"path\" : \"path\", \"code\" : \"code\", \"message\" : \"message\" }, { \"path\" : \"path\", \"code\" : \"code\", \"message\" : \"message\" } ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
