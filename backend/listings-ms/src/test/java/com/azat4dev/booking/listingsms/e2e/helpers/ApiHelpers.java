package com.azat4dev.booking.listingsms.e2e.helpers;

import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.function.Function;

public class ApiHelpers {

    public static <T> T apiClient(Function<ApiClient, T> factory, int port) {
        final var api = new ApiClient();
        api.setBasePath("http://localhost:" + port);
        return factory.apply(api);
    }

    public static <T> T apiClient(Function<ApiClient, T> factory, String accessToken, int port) {

        final var restClient = ApiClient.buildRestClientBuilder().messageConverters(
            converters -> {

                for (int i = 0; i < converters.size(); i++) {

                    final var converter = converters.get(i);
                    if (converter instanceof MappingJackson2HttpMessageConverter inst) {
                        final var isDefaultConverter = inst.getSupportedMediaTypes().contains(MediaType.APPLICATION_JSON) &&
                                                       !inst.getObjectMapper().getRegisteredModuleIds().contains("org.openapitools.jackson.nullable.JsonNullableModule");

                        if (!isDefaultConverter) {
                            continue;
                        }

                        // Remove default converter
                        converters.remove(i);
//                        converters.add(inst);
                        break;
                    }
                }
            }
        ).build();

        final var api = new ApiClient(restClient);
        api.setBasePath("http://localhost:" + port);
        api.setBearerToken(accessToken);
        return factory.apply(api);
    }
}
