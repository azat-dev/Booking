# CommandsResetPasswordApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**completeResetPassword**](CommandsResetPasswordApi.md#completeResetPassword) | **POST** /api/public/identity/password/set-new | Reset password by email |
| [**resetPasswordByEmail**](CommandsResetPasswordApi.md#resetPasswordByEmail) | **POST** /api/public/identity/password/reset | Reset password by email |



## completeResetPassword

> CompleteResetPassword200ResponseDTO completeResetPassword(completeResetPasswordRequestBodyDTO)

Reset password by email

### Example

```java
// Import classes:
import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.ApiException;
import com.azat4dev.booking.usersms.generated.client.base.Configuration;
import com.azat4dev.booking.usersms.generated.client.base.models.*;
import com.azat4dev.booking.usersms.generated.client.api.CommandsResetPasswordApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        CommandsResetPasswordApi apiInstance = new CommandsResetPasswordApi(defaultClient);
        CompleteResetPasswordRequestBodyDTO completeResetPasswordRequestBodyDTO = new CompleteResetPasswordRequestBodyDTO(); // CompleteResetPasswordRequestBodyDTO | JSON payload
        try {
            CompleteResetPassword200ResponseDTO result = apiInstance.completeResetPassword(completeResetPasswordRequestBodyDTO);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CommandsResetPasswordApi#completeResetPassword");
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
| **completeResetPasswordRequestBodyDTO** | [**CompleteResetPasswordRequestBodyDTO**](CompleteResetPasswordRequestBodyDTO.md)| JSON payload | |

### Return type

[**CompleteResetPassword200ResponseDTO**](CompleteResetPassword200ResponseDTO.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Set new password |  -  |
| **403** | Token is not valid |  -  |


## resetPasswordByEmail

> ResetPasswordByEmail200ResponseDTO resetPasswordByEmail(resetPasswordByEmailRequestBodyDTO)

Reset password by email

### Example

```java
// Import classes:
import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.base.ApiException;
import com.azat4dev.booking.usersms.generated.client.base.Configuration;
import com.azat4dev.booking.usersms.generated.client.base.models.*;
import com.azat4dev.booking.usersms.generated.client.api.CommandsResetPasswordApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost");

        CommandsResetPasswordApi apiInstance = new CommandsResetPasswordApi(defaultClient);
        ResetPasswordByEmailRequestBodyDTO resetPasswordByEmailRequestBodyDTO = new ResetPasswordByEmailRequestBodyDTO(); // ResetPasswordByEmailRequestBodyDTO | JSON payload
        try {
            ResetPasswordByEmail200ResponseDTO result = apiInstance.resetPasswordByEmail(resetPasswordByEmailRequestBodyDTO);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CommandsResetPasswordApi#resetPasswordByEmail");
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
| **resetPasswordByEmailRequestBodyDTO** | [**ResetPasswordByEmailRequestBodyDTO**](ResetPasswordByEmailRequestBodyDTO.md)| JSON payload | |

### Return type

[**ResetPasswordByEmail200ResponseDTO**](ResetPasswordByEmail200ResponseDTO.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Sent email with reset password instructions |  -  |
| **401** | Not valid email |  -  |
| **404** | Email not found |  -  |

