package com.azat4dev.booking.usersms.generated.client.api;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;

import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.client.model.GenerateUploadUserPhotoUrlResponseBodyDTO;
import com.azat4dev.booking.usersms.generated.client.model.UpdateUserPhoto200ResponseDTO;
import com.azat4dev.booking.usersms.generated.client.model.UpdateUserPhotoRequestBodyDTO;

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
public class CommandsUpdateUserPhotoApi {
    private ApiClient apiClient;

    public CommandsUpdateUserPhotoApi() {
        this(new ApiClient());
    }

    @Autowired
    public CommandsUpdateUserPhotoApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Generate upload form for user photo
     * 
     * <p><b>200</b> - OK
     * @param generateUploadUserPhotoUrlRequestBodyDTO The generateUploadUserPhotoUrlRequestBodyDTO parameter
     * @return GenerateUploadUserPhotoUrlResponseBodyDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec generateUploadUserPhotoUrlRequestCreation(GenerateUploadUserPhotoUrlRequestBodyDTO generateUploadUserPhotoUrlRequestBodyDTO) throws RestClientResponseException {
        Object postBody = generateUploadUserPhotoUrlRequestBodyDTO;
        // verify the required parameter 'generateUploadUserPhotoUrlRequestBodyDTO' is set
        if (generateUploadUserPhotoUrlRequestBodyDTO == null) {
            throw new RestClientResponseException("Missing the required parameter 'generateUploadUserPhotoUrlRequestBodyDTO' when calling generateUploadUserPhotoUrl", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        String[] localVarAuthNames = new String[] { "BearerAuth" };

        ParameterizedTypeReference<GenerateUploadUserPhotoUrlResponseBodyDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/api/private/identity/users/current/photo/get-upload-url", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Generate upload form for user photo
     * 
     * <p><b>200</b> - OK
     * @param generateUploadUserPhotoUrlRequestBodyDTO The generateUploadUserPhotoUrlRequestBodyDTO parameter
     * @return GenerateUploadUserPhotoUrlResponseBodyDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public GenerateUploadUserPhotoUrlResponseBodyDTO generateUploadUserPhotoUrl(GenerateUploadUserPhotoUrlRequestBodyDTO generateUploadUserPhotoUrlRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<GenerateUploadUserPhotoUrlResponseBodyDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return generateUploadUserPhotoUrlRequestCreation(generateUploadUserPhotoUrlRequestBodyDTO).body(localVarReturnType);
    }

    /**
     * Generate upload form for user photo
     * 
     * <p><b>200</b> - OK
     * @param generateUploadUserPhotoUrlRequestBodyDTO The generateUploadUserPhotoUrlRequestBodyDTO parameter
     * @return ResponseEntity&lt;GenerateUploadUserPhotoUrlResponseBodyDTO&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<GenerateUploadUserPhotoUrlResponseBodyDTO> generateUploadUserPhotoUrlWithHttpInfo(GenerateUploadUserPhotoUrlRequestBodyDTO generateUploadUserPhotoUrlRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<GenerateUploadUserPhotoUrlResponseBodyDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return generateUploadUserPhotoUrlRequestCreation(generateUploadUserPhotoUrlRequestBodyDTO).toEntity(localVarReturnType);
    }

    /**
     * Generate upload form for user photo
     * 
     * <p><b>200</b> - OK
     * @param generateUploadUserPhotoUrlRequestBodyDTO The generateUploadUserPhotoUrlRequestBodyDTO parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec generateUploadUserPhotoUrlWithResponseSpec(GenerateUploadUserPhotoUrlRequestBodyDTO generateUploadUserPhotoUrlRequestBodyDTO) throws RestClientResponseException {
        return generateUploadUserPhotoUrlRequestCreation(generateUploadUserPhotoUrlRequestBodyDTO);
    }
    /**
     * Attach uploaded photo to the user
     * 
     * <p><b>200</b> - Photo updated
     * @param updateUserPhotoRequestBodyDTO The updateUserPhotoRequestBodyDTO parameter
     * @return UpdateUserPhoto200ResponseDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec updateUserPhotoRequestCreation(UpdateUserPhotoRequestBodyDTO updateUserPhotoRequestBodyDTO) throws RestClientResponseException {
        Object postBody = updateUserPhotoRequestBodyDTO;
        // verify the required parameter 'updateUserPhotoRequestBodyDTO' is set
        if (updateUserPhotoRequestBodyDTO == null) {
            throw new RestClientResponseException("Missing the required parameter 'updateUserPhotoRequestBodyDTO' when calling updateUserPhoto", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        String[] localVarAuthNames = new String[] { "BearerAuth" };

        ParameterizedTypeReference<UpdateUserPhoto200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/api/private/identity/users/current/photo/update", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Attach uploaded photo to the user
     * 
     * <p><b>200</b> - Photo updated
     * @param updateUserPhotoRequestBodyDTO The updateUserPhotoRequestBodyDTO parameter
     * @return UpdateUserPhoto200ResponseDTO
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public UpdateUserPhoto200ResponseDTO updateUserPhoto(UpdateUserPhotoRequestBodyDTO updateUserPhotoRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<UpdateUserPhoto200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return updateUserPhotoRequestCreation(updateUserPhotoRequestBodyDTO).body(localVarReturnType);
    }

    /**
     * Attach uploaded photo to the user
     * 
     * <p><b>200</b> - Photo updated
     * @param updateUserPhotoRequestBodyDTO The updateUserPhotoRequestBodyDTO parameter
     * @return ResponseEntity&lt;UpdateUserPhoto200ResponseDTO&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<UpdateUserPhoto200ResponseDTO> updateUserPhotoWithHttpInfo(UpdateUserPhotoRequestBodyDTO updateUserPhotoRequestBodyDTO) throws RestClientResponseException {
        ParameterizedTypeReference<UpdateUserPhoto200ResponseDTO> localVarReturnType = new ParameterizedTypeReference<>() {};
        return updateUserPhotoRequestCreation(updateUserPhotoRequestBodyDTO).toEntity(localVarReturnType);
    }

    /**
     * Attach uploaded photo to the user
     * 
     * <p><b>200</b> - Photo updated
     * @param updateUserPhotoRequestBodyDTO The updateUserPhotoRequestBodyDTO parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec updateUserPhotoWithResponseSpec(UpdateUserPhotoRequestBodyDTO updateUserPhotoRequestBodyDTO) throws RestClientResponseException {
        return updateUserPhotoRequestCreation(updateUserPhotoRequestBodyDTO);
    }
}
