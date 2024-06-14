package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;

import com.azat4dev.booking.usersms.generated.client.model.CompleteResetPassword200ResponseDTO;
import com.azat4dev.booking.usersms.generated.client.model.CompleteResetPasswordRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.client.model.ResetPasswordByEmail200ResponseDTO;
import com.azat4dev.booking.usersms.generated.client.model.ResetPasswordByEmailRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.client.model.VerifyEmail400ResponseDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-06-14T09:11:45.773021+03:00[Europe/Moscow]", comments = "Generator version: 7.6.0")
public class CommandsResetPasswordApi {
    private ApiClient apiClient;

    public CommandsResetPasswordApi() {
        this(new ApiClient());
    }

    @Autowired
    public CommandsResetPasswordApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Reset password by email
     * 
     * <p><b>200</b> - Set new password
     * <p><b>403</b> - Token is not valid
     * @param completeResetPasswordRequestBodyDTO JSON payload
     * @return CompleteResetPassword200ResponseDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec completeResetPasswordRequestCreation(CompleteResetPasswordRequestBodyDTO completeResetPasswordRequestBodyDTO) throws RestClientResponseException {
        Object postBody = completeResetPasswordRequestBodyDTO;
        // verify the required parameter 'completeResetPasswordRequestBodyDTO' is set
        if (completeResetPasswordRequestBodyDTO == null) {
            throw new RestClientResponseException("Missing the required parameter 'completeResetPasswordRequestBodyDTO' when calling completeResetPassword", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<CompleteResetPassword200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/api/public/identity/password/set-new", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Reset password by email
     * 
     * <p><b>200</b> - Set new password
     * <p><b>403</b> - Token is not valid
     * @param completeResetPasswordRequestBodyDTO JSON payload
     * @return CompleteResetPassword200ResponseDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CompleteResetPassword200ResponseDTO completeResetPassword(CompleteResetPasswordRequestBodyDTO completeResetPasswordRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<CompleteResetPassword200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return completeResetPasswordRequestCreation(completeResetPasswordRequestBodyDTO).body(localVarReturnType);
    }

    /**
     * Reset password by email
     * 
     * <p><b>200</b> - Set new password
     * <p><b>403</b> - Token is not valid
     * @param completeResetPasswordRequestBodyDTO JSON payload
     * @return ResponseEntity&lt;CompleteResetPassword200ResponseDTO&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CompleteResetPassword200ResponseDTO> completeResetPasswordWithHttpInfo(CompleteResetPasswordRequestBodyDTO completeResetPasswordRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<CompleteResetPassword200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return completeResetPasswordRequestCreation(completeResetPasswordRequestBodyDTO).toEntity(localVarReturnType);
    }

    /**
     * Reset password by email
     * 
     * <p><b>200</b> - Set new password
     * <p><b>403</b> - Token is not valid
     * @param completeResetPasswordRequestBodyDTO JSON payload
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec completeResetPasswordWithResponseSpec(CompleteResetPasswordRequestBodyDTO completeResetPasswordRequestBodyDTO) throws RestClientResponseException {
        return completeResetPasswordRequestCreation(completeResetPasswordRequestBodyDTO);
    }
    /**
     * Reset password by email
     * 
     * <p><b>200</b> - Sent email with reset password instructions
     * <p><b>401</b> - Not valid email
     * <p><b>404</b> - Email not found
     * @param resetPasswordByEmailRequestBodyDTO JSON payload
     * @return ResetPasswordByEmail200ResponseDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec resetPasswordByEmailRequestCreation(ResetPasswordByEmailRequestBodyDTO resetPasswordByEmailRequestBodyDTO) throws RestClientResponseException {
        Object postBody = resetPasswordByEmailRequestBodyDTO;
        // verify the required parameter 'resetPasswordByEmailRequestBodyDTO' is set
        if (resetPasswordByEmailRequestBodyDTO == null) {
            throw new RestClientResponseException("Missing the required parameter 'resetPasswordByEmailRequestBodyDTO' when calling resetPasswordByEmail", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<ResetPasswordByEmail200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/api/public/identity/password/reset", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Reset password by email
     * 
     * <p><b>200</b> - Sent email with reset password instructions
     * <p><b>401</b> - Not valid email
     * <p><b>404</b> - Email not found
     * @param resetPasswordByEmailRequestBodyDTO JSON payload
     * @return ResetPasswordByEmail200ResponseDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResetPasswordByEmail200ResponseDTO resetPasswordByEmail(ResetPasswordByEmailRequestBodyDTO resetPasswordByEmailRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<ResetPasswordByEmail200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return resetPasswordByEmailRequestCreation(resetPasswordByEmailRequestBodyDTO).body(localVarReturnType);
    }

    /**
     * Reset password by email
     * 
     * <p><b>200</b> - Sent email with reset password instructions
     * <p><b>401</b> - Not valid email
     * <p><b>404</b> - Email not found
     * @param resetPasswordByEmailRequestBodyDTO JSON payload
     * @return ResponseEntity&lt;ResetPasswordByEmail200ResponseDTO&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ResetPasswordByEmail200ResponseDTO> resetPasswordByEmailWithHttpInfo(ResetPasswordByEmailRequestBodyDTO resetPasswordByEmailRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<ResetPasswordByEmail200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return resetPasswordByEmailRequestCreation(resetPasswordByEmailRequestBodyDTO).toEntity(localVarReturnType);
    }

    /**
     * Reset password by email
     * 
     * <p><b>200</b> - Sent email with reset password instructions
     * <p><b>401</b> - Not valid email
     * <p><b>404</b> - Email not found
     * @param resetPasswordByEmailRequestBodyDTO JSON payload
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec resetPasswordByEmailWithResponseSpec(ResetPasswordByEmailRequestBodyDTO resetPasswordByEmailRequestBodyDTO) throws RestClientResponseException {
        return resetPasswordByEmailRequestCreation(resetPasswordByEmailRequestBodyDTO);
    }
}
