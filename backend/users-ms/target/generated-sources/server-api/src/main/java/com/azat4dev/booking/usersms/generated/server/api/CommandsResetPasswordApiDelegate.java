package com.azat4dev.booking.usersms.generated.server.api;

import com.azat4dev.booking.usersms.generated.server.model.CompleteResetPassword200ResponseDTO;
import com.azat4dev.booking.usersms.generated.server.model.CompleteResetPasswordRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.ResetPasswordByEmail200ResponseDTO;
import com.azat4dev.booking.usersms.generated.server.model.ResetPasswordByEmailRequestBodyDTO;
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
 * A delegate to be called by the {@link CommandsResetPasswordApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
public interface CommandsResetPasswordApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /api/public/identity/password/set-new : Reset password by email
     *
     * @param completeResetPasswordRequestBodyDTO JSON payload (required)
     * @return Set new password (status code 200)
     *         or Token is not valid (status code 403)
     * @see CommandsResetPasswordApi#completeResetPassword
     */
    default ResponseEntity<CompleteResetPassword200ResponseDTO> completeResetPassword(CompleteResetPasswordRequestBodyDTO completeResetPasswordRequestBodyDTO) throws Exception {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Set new password\" }";
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

    /**
     * POST /api/public/identity/password/reset : Reset password by email
     *
     * @param resetPasswordByEmailRequestBodyDTO JSON payload (required)
     * @return Sent email with reset password instructions (status code 200)
     *         or Not valid email (status code 401)
     *         or Email not found (status code 404)
     * @see CommandsResetPasswordApi#resetPasswordByEmail
     */
    default ResponseEntity<ResetPasswordByEmail200ResponseDTO> resetPasswordByEmail(ResetPasswordByEmailRequestBodyDTO resetPasswordByEmailRequestBodyDTO) throws Exception {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"message\" : \"Sent email with reset password instructions\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"type\" : \"validationError\", \"errors\" : [ { \"path\" : \"path\", \"code\" : \"code\", \"message\" : \"message\" }, { \"path\" : \"path\", \"code\" : \"code\", \"message\" : \"message\" } ] }";
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
