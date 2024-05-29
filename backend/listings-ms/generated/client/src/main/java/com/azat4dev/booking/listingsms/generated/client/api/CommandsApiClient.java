package com.azat4dev.booking.listingsms.generated.client.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.openapitools.configuration.ClientConfiguration;

@FeignClient(name="${commands.name:commands}", url="${commands.url:http://localhost}", configuration = ClientConfiguration.class)
public interface CommandsApiClient extends CommandsApi {
}
