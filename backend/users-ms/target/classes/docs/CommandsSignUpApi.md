# CommandsSignUpApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**signUpByEmail**](CommandsSignUpApi.md#signUpByEmail) | **POST** /api/public/identity/sign-up | Sign up a new user |



## signUpByEmail

> SignUpByEmailResponseBodyDTO signUpByEmail(signUpByEmailRequestBodyDTO)

Sign up a new user

### Example

```java
// Import classes:
import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.ApiException;
import com.azat4dev.booking.usersms.generated.client.base.Configuration;
import com.azat4dev.booking.usersms.generated.client.base.models.*;
import com.azat4dev.booking.usersms.generated.client.api.CommandsSignUpApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        CommandsSignUpApi apiInstance = new CommandsSignUpApi(defaultClient);
        SignUpByEmailRequestBodyDTO signUpByEmailRequestBodyDTO = new SignUpByEmailRequestBodyDTO(); // SignUpByEmailRequestBodyDTO | JSON payload
        try {
            SignUpByEmailResponseBodyDTO result = apiInstance.signUpByEmail(signUpByEmailRequestBodyDTO);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CommandsSignUpApi#signUpByEmail");
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
| **signUpByEmailRequestBodyDTO** | [**SignUpByEmailRequestBodyDTO**](SignUpByEmailRequestBodyDTO.md)| JSON payload | |

### Return type

[**SignUpByEmailResponseBodyDTO**](SignUpByEmailResponseBodyDTO.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Success |  -  |
| **400** | Wrong request |  -  |
| **409** | Conflict - User with provided email already exists |  -  |

