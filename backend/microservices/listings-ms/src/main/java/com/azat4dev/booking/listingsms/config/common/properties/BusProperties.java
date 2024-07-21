package com.azat4dev.booking.listingsms.config.common.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.bus")
@Data
public class BusProperties {

    @NotBlank
    @NotNull
    private final String eventsTopicPrefix;
}
