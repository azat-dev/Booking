package com.azat4dev.booking.helpers;

import com.azat4dev.booking.usersms.generated.client.base.ApiClient;

import java.util.function.Function;

public class ApiHelpers {

    public static  <T> T apiClient(Function<ApiClient, T> factory, int port) {
        final var api = new ApiClient();
        api.setBasePath("http://localhost:" + port);
        return factory.apply(api);
    }

    public static  <T> T apiClient(Function<ApiClient, T> factory, String accessToken, int port) {
        final var api = new ApiClient();
        api.setBasePath("http://localhost:" + port);
        api.setBearerToken(accessToken);
        return factory.apply(api);
    }
}
