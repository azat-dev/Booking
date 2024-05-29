package com.azat4dev.booking.listingsms.generated.client.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.openapitools.configuration.ClientConfiguration;

@FeignClient(name="${queries.name:queries}", url="${queries.url:http://localhost}", configuration = ClientConfiguration.class)
public interface QueriesApiClient extends QueriesApi {
}
