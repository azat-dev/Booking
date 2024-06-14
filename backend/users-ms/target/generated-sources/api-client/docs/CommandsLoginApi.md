# CommandsLoginApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**loginByEmail**](CommandsLoginApi.md#loginByEmail) | **POST** /api/public/identity/login | Get a new pair of tokens by email (access, refresh) |



## loginByEmail

> LoginByEmailResponseBodyDTO loginByEmail(loginByEmailRequestBodyDTO)

Get a new pair of tokens by email (access, refresh)

### Example

```java
// Import classes:
import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.ApiException;
import com.azat4dev.booking.usersms.generated.client.base.Configuration;
import com.azat4dev.booking.usersms.generated.client.base.models.*;
import com.azat4dev.booking.usersms.generated.client.api.CommandsLoginApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        CommandsLoginApi apiInstance = new CommandsLoginApi(defaultClient);
        LoginByEmailRequestBodyDTO loginByEmailRequestBodyDTO = new LoginByEmailRequestBodyDTO(); // LoginByEmailRequestBodyDTO | JSON payload
        try {
            LoginByEmailResponseBodyDTO result = apiInstance.loginByEmail(loginByEmailRequestBodyDTO);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CommandsLoginApi#loginByEmail");
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
| **loginByEmailRequestBodyDTO** | [**LoginByEmailRequestBodyDTO**](LoginByEmailRequestBodyDTO.md)| JSON payload | |

### Return type

[**LoginByEmailResponseBodyDTO**](LoginByEmailResponseBodyDTO.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **403** | User not found |  -  |

