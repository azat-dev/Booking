# QueriesCurrentUserApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getCurrentUser**](QueriesCurrentUserApi.md#getCurrentUser) | **GET** /api/private/identity/users/current | Gets current user info |



## getCurrentUser

> PersonalUserInfoDTO getCurrentUser()

Gets current user info

### Example

```java
// Import classes:
import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.ApiException;
import com.azat4dev.booking.usersms.generated.client.base.Configuration;
import com.azat4dev.booking.usersms.generated.client.base.auth.*;
import com.azat4dev.booking.usersms.generated.client.base.models.*;
import com.azat4dev.booking.usersms.generated.client.api.QueriesCurrentUserApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");
        
        // Configure HTTP bearer authorization: BearerAuth
        HttpBearerAuth BearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("BearerAuth");
        BearerAuth.setBearerToken("BEARER TOKEN");

        QueriesCurrentUserApi apiInstance = new QueriesCurrentUserApi(defaultClient);
        try {
            PersonalUserInfoDTO result = apiInstance.getCurrentUser();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling QueriesCurrentUserApi#getCurrentUser");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**PersonalUserInfoDTO**](PersonalUserInfoDTO.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Current users info |  -  |
| **401** | User not authenticated |  -  |
| **404** | User not found |  -  |

