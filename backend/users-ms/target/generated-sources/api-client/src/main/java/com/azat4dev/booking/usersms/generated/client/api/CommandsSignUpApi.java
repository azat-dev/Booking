package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;

import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmailRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.client.model.SignUpByEmailResponseBodyDTO;
import com.azat4dev.booking.usersms.generated.client.model.UserWithSameEmailAlreadyExistsErrorDTO;
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
public class CommandsSignUpApi {
    private ApiClient apiClient;

    public CommandsSignUpApi() {
        this(new ApiClient());
    }

    @Autowired
    public CommandsSignUpApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Sign up a new user
     * 
     * <p><b>201</b> - Success
     * <p><b>400</b> - Wrong request
     * <p><b>409</b> - Conflict - User with provided email already exists
     * @param signUpByEmailRequestBodyDTO JSON payload
     * @return SignUpByEmailResponseBodyDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec signUpByEmailRequestCreation(SignUpByEmailRequestBodyDTO signUpByEmailRequestBodyDTO) throws RestClientResponseException {
        Object postBody = signUpByEmailRequestBodyDTO;
        // verify the required parameter 'signUpByEmailRequestBodyDTO' is set
        if (signUpByEmailRequestBodyDTO == null) {
            throw new RestClientResponseException("Missing the required parameter 'signUpByEmailRequestBodyDTO' when calling signUpByEmail", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<SignUpByEmailResponseBodyDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/api/public/identity/sign-up", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Sign up a new user
     * 
     * <p><b>201</b> - Success
     * <p><b>400</b> - Wrong request
     * <p><b>409</b> - Conflict - User with provided email already exists
     * @param signUpByEmailRequestBodyDTO JSON payload
     * @return SignUpByEmailResponseBodyDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public SignUpByEmailResponseBodyDTO signUpByEmail(SignUpByEmailRequestBodyDTO signUpByEmailRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<SignUpByEmailResponseBodyDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return signUpByEmailRequestCreation(signUpByEmailRequestBodyDTO).body(localVarReturnType);
    }

    /**
     * Sign up a new user
     * 
     * <p><b>201</b> - Success
     * <p><b>400</b> - Wrong request
     * <p><b>409</b> - Conflict - User with provided email already exists
     * @param signUpByEmailRequestBodyDTO JSON payload
     * @return ResponseEntity&lt;SignUpByEmailResponseBodyDTO&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SignUpByEmailResponseBodyDTO> signUpByEmailWithHttpInfo(SignUpByEmailRequestBodyDTO signUpByEmailRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<SignUpByEmailResponseBodyDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return signUpByEmailRequestCreation(signUpByEmailRequestBodyDTO).toEntity(localVarReturnType);
    }

    /**
     * Sign up a new user
     * 
     * <p><b>201</b> - Success
     * <p><b>400</b> - Wrong request
     * <p><b>409</b> - Conflict - User with provided email already exists
     * @param signUpByEmailRequestBodyDTO JSON payload
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec signUpByEmailWithResponseSpec(SignUpByEmailRequestBodyDTO signUpByEmailRequestBodyDTO) throws RestClientResponseException {
        return signUpByEmailRequestCreation(signUpByEmailRequestBodyDTO);
    }
}
