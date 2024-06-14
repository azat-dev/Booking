package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;

import com.azat4dev.booking.usersms.generated.client.model.VerifyEmail200ResponseDTO;
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
public class CommandsEmailVerificationApi {
    private ApiClient apiClient;

    public CommandsEmailVerificationApi() {
        this(new ApiClient());
    }

    @Autowired
    public CommandsEmailVerificationApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Send email for verification
     * 
     * <p><b>200</b> - Email verified
     * <p><b>400</b> - Invalid token
     * <p><b>403</b> - User not found
     * @param token The token parameter
     * @return VerifyEmail200ResponseDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec verifyEmailRequestCreation(String token) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'token' is set
        if (token == null) {
            throw new RestClientResponseException("Missing the required parameter 'token' when calling verifyEmail", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "token", token));
        
        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "BearerAuth" };

        ParameterizedTypeReference<VerifyEmail200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/api/public/identity/verify-email", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Send email for verification
     * 
     * <p><b>200</b> - Email verified
     * <p><b>400</b> - Invalid token
     * <p><b>403</b> - User not found
     * @param token The token parameter
     * @return VerifyEmail200ResponseDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public VerifyEmail200ResponseDTO verifyEmail(String token) throws RestClientResponseException {
        ParameterizedTypeReference<VerifyEmail200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return verifyEmailRequestCreation(token).body(localVarReturnType);
    }

    /**
     * Send email for verification
     * 
     * <p><b>200</b> - Email verified
     * <p><b>400</b> - Invalid token
     * <p><b>403</b> - User not found
     * @param token The token parameter
     * @return ResponseEntity&lt;VerifyEmail200ResponseDTO&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<VerifyEmail200ResponseDTO> verifyEmailWithHttpInfo(String token) throws RestClientResponseException {
        ParameterizedTypeReference<VerifyEmail200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return verifyEmailRequestCreation(token).toEntity(localVarReturnType);
    }

    /**
     * Send email for verification
     * 
     * <p><b>200</b> - Email verified
     * <p><b>400</b> - Invalid token
     * <p><b>403</b> - User not found
     * @param token The token parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec verifyEmailWithResponseSpec(String token) throws RestClientResponseException {
        return verifyEmailRequestCreation(token);
    }
}
