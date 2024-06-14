package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;

import com.azat4dev.booking.usersms.generated.client.model.PersonalUserInfoDTO;

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
public class QueriesCurrentUserApi {
    private ApiClient apiClient;

    public QueriesCurrentUserApi() {
        this(new ApiClient());
    }

    @Autowired
    public QueriesCurrentUserApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Gets current user info
     * 
     * <p><b>200</b> - Current users info
     * <p><b>401</b> - User not authenticated
     * <p><b>404</b> - User not found
     * @return PersonalUserInfoDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getCurrentUserRequestCreation() throws RestClientResponseException {
        Object postBody = null;
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
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "BearerAuth" };

        ParameterizedTypeReference<PersonalUserInfoDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/api/private/identity/users/current", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Gets current user info
     * 
     * <p><b>200</b> - Current users info
     * <p><b>401</b> - User not authenticated
     * <p><b>404</b> - User not found
     * @return PersonalUserInfoDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public PersonalUserInfoDTO getCurrentUser() throws RestClientResponseException {
        ParameterizedTypeReference<PersonalUserInfoDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return getCurrentUserRequestCreation().body(localVarReturnType);
    }

    /**
     * Gets current user info
     * 
     * <p><b>200</b> - Current users info
     * <p><b>401</b> - User not authenticated
     * <p><b>404</b> - User not found
     * @return ResponseEntity&lt;PersonalUserInfoDTO&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<PersonalUserInfoDTO> getCurrentUserWithHttpInfo() throws RestClientResponseException {
        ParameterizedTypeReference<PersonalUserInfoDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return getCurrentUserRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Gets current user info
     * 
     * <p><b>200</b> - Current users info
     * <p><b>401</b> - User not authenticated
     * <p><b>404</b> - User not found
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getCurrentUserWithResponseSpec() throws RestClientResponseException {
        return getCurrentUserRequestCreation();
    }
}
