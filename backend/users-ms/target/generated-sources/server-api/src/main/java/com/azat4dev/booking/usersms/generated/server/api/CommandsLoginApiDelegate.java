package com.azat4dev.booking.usersms.generated.server.api;

import com.azat4dev.booking.usersms.generated.server.model.LoginByEmailRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.LoginByEmailResponseBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.VerifyEmail400ResponseDTO;
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
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public interface CommandsLoginApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /api/public/identity/login : Get a new pair of tokens by email (access, refresh)
     *
     * @param loginByEmailRequestBodyDTO JSON payload (required)
     * @return Success (status code 200)
     *         or User not found (status code 403)
     * @see CommandsLoginApi#loginByEmail
     */
    default ResponseEntity<LoginByEmailResponseBodyDTO> loginByEmail(LoginByEmailRequestBodyDTO loginByEmailRequestBodyDTO) throws Exception {
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
