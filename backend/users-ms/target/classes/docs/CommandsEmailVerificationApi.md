# CommandsEmailVerificationApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**verifyEmail**](CommandsEmailVerificationApi.md#verifyEmail) | **GET** /api/public/identity/verify-email | Send email for verification |



## verifyEmail

> VerifyEmail200ResponseDTO verifyEmail(token)

Send email for verification

### Example

```java
// Import classes:
import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.ApiException;
import com.azat4dev.booking.usersms.generated.client.base.Configuration;
import com.azat4dev.booking.usersms.generated.client.base.auth.*;
import com.azat4dev.booking.usersms.generated.client.base.models.*;
import com.azat4dev.booking.usersms.generated.client.api.CommandsEmailVerificationApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");
        
        // Configure HTTP bearer authorization: BearerAuth
        HttpBearerAuth BearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("BearerAuth");
        BearerAuth.setBearerToken("BEARER TOKEN");

        CommandsEmailVerificationApi apiInstance = new CommandsEmailVerificationApi(defaultClient);
        String token = "token_example"; // String | 
        try {
            VerifyEmail200ResponseDTO result = apiInstance.verifyEmail(token);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CommandsEmailVerificationApi#verifyEmail");
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
| **token** | **String**|  | |

### Return type

[**VerifyEmail200ResponseDTO**](VerifyEmail200ResponseDTO.md)

### Authorization

[BearerAuth](../README.md#BearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Email verified |  -  |
| **400** | Invalid token |  -  |
| **403** | User not found |  -  |

