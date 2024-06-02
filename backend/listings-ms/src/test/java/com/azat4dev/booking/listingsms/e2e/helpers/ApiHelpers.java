package com.azat4dev.booking.listingsms.e2e.helpers;

import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;

public class ApiHelpers {

    public static  <T extends ApiClient.Api> T apiClient(Class<T> apiClass, int port) {
        final var api = new ApiClient();
        api.setBasePath("http://localhost:" + port);
        return api.buildClient(apiClass);
    }

    public static  <T extends ApiClient.Api> T apiClient(Class<T> apiClass, String accessToken, int port) {
        final var api = new ApiClient("BearerAuth");
        api.setBearerToken(accessToken);
        api.setBasePath("http://localhost:" + port);
        return api.buildClient(apiClass);
    }
}
