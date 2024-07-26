package com.azat4dev.booking.listingsms.config.common.properties;

import jakarta.validation.constraints.Min;
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
    private final String prefixForEventsTopics;

    @NotBlank
    @NotNull
    private final String listingEventsTopicName;

    private final Short listingEventsTopicReplicationFactor;

    @Min(1)
    @NotNull
    private final Integer listingEventsTopicNumberOfConsumers;

    @Min(1)
    @NotNull
    private final Integer listingEventsTopicNumberOfPartitions;

    @NotBlank
    @NotNull
    private final String internalCommandsTopicPrefix;

    @Min(1)
    @NotNull
    private final Integer internalCommandsTopicNumberOfPartitions;

    private final Short internalCommandsTopicReplicationFactor;

    @Min(1)
    @NotNull
    private final Integer internalCommandsTopicNumberOfConsumers;

    @NotBlank
    public final String publicCommandsTopicPrefix;

    @NotBlank
    public final String publicCommandsGetListingPublicDetailsByIdTopicName;
}
