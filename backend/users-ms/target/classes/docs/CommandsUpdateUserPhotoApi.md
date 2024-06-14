# CommandsUpdateUserPhotoApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**generateUploadUserPhotoUrl**](CommandsUpdateUserPhotoApi.md#generateUploadUserPhotoUrl) | **POST** /api/private/identity/users/current/photo/get-upload-url | Generate upload form for user photo |
| [**updateUserPhoto**](CommandsUpdateUserPhotoApi.md#updateUserPhoto) | **POST** /api/private/identity/users/current/photo/update | Attach uploaded photo to the user |



## generateUploadUserPhotoUrl

> GenerateUploadUserPhotoUrlResponseBodyDTO generateUploadUserPhotoUrl(generateUploadUserPhotoUrlRequestBodyDTO)

Generate upload form for user photo

### Example

```java
// Import classes:
import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.ApiException;
import com.azat4dev.booking.usersms.generated.client.base.Configuration;
import com.azat4dev.booking.usersms.generated.client.base.auth.*;
import com.azat4dev.booking.usersms.generated.client.base.models.*;
import com.azat4dev.booking.usersms.generated.client.api.CommandsUpdateUserPhotoApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");
        
        // Configure HTTP bearer authorization: BearerAuth
        HttpBearerAuth BearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("BearerAuth");
        BearerAuth.setBearerToken("BEARER TOKEN");

        CommandsUpdateUserPhotoApi apiInstance = new CommandsUpdateUserPhotoApi(defaultClient);
        GenerateUploadUserPhotoUrlRequestBodyDTO generateUploadUserPhotoUrlRequestBodyDTO = new GenerateUploadUserPhotoUrlRequestBodyDTO(); // GenerateUploadUserPhotoUrlRequestBodyDTO | 
        try {
            GenerateUploadUserPhotoUrlResponseBodyDTO result = apiInstance.generateUploadUserPhotoUrl(generateUploadUserPhotoUrlRequestBodyDTO);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CommandsUpdateUserPhotoApi#generateUploadUserPhotoUrl");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **generateUploadUserPhotoUrlRequestBodyDTO** | [**GenerateUploadUserPhotoUrlRequestBodyDTO**](GenerateUploadUserPhotoUrlRequestBodyDTO.md)|  | |

### Return type

[**GenerateUploadUserPhotoUrlResponseBodyDTO**](GenerateUploadUserPhotoUrlResponseBodyDTO.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## updateUserPhoto

> UpdateUserPhoto200ResponseDTO updateUserPhoto(updateUserPhotoRequestBodyDTO)

Attach uploaded photo to the user

### Example

```java
// Import classes:
import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.ApiException;
import com.azat4dev.booking.usersms.generated.client.base.Configuration;
import com.azat4dev.booking.usersms.generated.client.base.auth.*;
import com.azat4dev.booking.usersms.generated.client.base.models.*;
import com.azat4dev.booking.usersms.generated.client.api.CommandsUpdateUserPhotoApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");
        
        // Configure HTTP bearer authorization: BearerAuth
        HttpBearerAuth BearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("BearerAuth");
        BearerAuth.setBearerToken("BEARER TOKEN");

        CommandsUpdateUserPhotoApi apiInstance = new CommandsUpdateUserPhotoApi(defaultClient);
        UpdateUserPhotoRequestBodyDTO updateUserPhotoRequestBodyDTO = new UpdateUserPhotoRequestBodyDTO(); // UpdateUserPhotoRequestBodyDTO | 
        try {
            UpdateUserPhoto200ResponseDTO result = apiInstance.updateUserPhoto(updateUserPhotoRequestBodyDTO);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CommandsUpdateUserPhotoApi#updateUserPhoto");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **updateUserPhotoRequestBodyDTO** | [**UpdateUserPhotoRequestBodyDTO**](UpdateUserPhotoRequestBodyDTO.md)|  | |

### Return type

[**UpdateUserPhoto200ResponseDTO**](UpdateUserPhoto200ResponseDTO.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Photo updated |  -  |

