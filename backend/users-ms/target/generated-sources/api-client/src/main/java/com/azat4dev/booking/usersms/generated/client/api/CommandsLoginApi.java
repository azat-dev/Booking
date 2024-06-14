package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;

import com.azat4dev.booking.usersms.generated.client.model.LoginByEmailRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.client.model.LoginByEmailResponseBodyDTO;
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
public class CommandsLoginApi {
    private ApiClient apiClient;

    public CommandsLoginApi() {
        this(new ApiClient());
    }

    @Autowired
    public CommandsLoginApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get a new pair of tokens by email (access, refresh)
     * 
     * <p><b>200</b> - Success
     * <p><b>403</b> - User not found
     * @param loginByEmailRequestBodyDTO JSON payload
     * @return LoginByEmailResponseBodyDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec loginByEmailRequestCreation(LoginByEmailRequestBodyDTO loginByEmailRequestBodyDTO) throws RestClientResponseException {
        Object postBody = loginByEmailRequestBodyDTO;
        // verify the required parameter 'loginByEmailRequestBodyDTO' is set
        if (loginByEmailRequestBodyDTO == null) {
            throw new RestClientResponseException("Missing the required parameter 'loginByEmailRequestBodyDTO' when calling loginByEmail", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<LoginByEmailResponseBodyDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/api/public/identity/login", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a new pair of tokens by email (access, refresh)
     * 
     * <p><b>200</b> - Success
     * <p><b>403</b> - User not found
     * @param loginByEmailRequestBodyDTO JSON payload
     * @return LoginByEmailResponseBodyDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public LoginByEmailResponseBodyDTO loginByEmail(LoginByEmailRequestBodyDTO loginByEmailRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<LoginByEmailResponseBodyDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return loginByEmailRequestCreation(loginByEmailRequestBodyDTO).body(localVarReturnType);
    }

    /**
     * Get a new pair of tokens by email (access, refresh)
     * 
     * <p><b>200</b> - Success
     * <p><b>403</b> - User not found
     * @param loginByEmailRequestBodyDTO JSON payload
     * @return ResponseEntity&lt;LoginByEmailResponseBodyDTO&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<LoginByEmailResponseBodyDTO> loginByEmailWithHttpInfo(LoginByEmailRequestBodyDTO loginByEmailRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<LoginByEmailResponseBodyDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return loginByEmailRequestCreation(loginByEmailRequestBodyDTO).toEntity(localVarReturnType);
    }

    /**
     * Get a new pair of tokens by email (access, refresh)
     * 
     * <p><b>200</b> - Success
     * <p><b>403</b> - User not found
     * @param loginByEmailRequestBodyDTO JSON payload
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec loginByEmailWithResponseSpec(LoginByEmailRequestBodyDTO loginByEmailRequestBodyDTO) throws RestClientResponseException {
        return loginByEmailRequestCreation(loginByEmailRequestBodyDTO);
    }
}
