package com.azat4dev.booking.searchlistingsms.config.common.properties;

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
    private final String prefixForEventsChannels;

    @NotBlank
    @NotNull
    private final String listingEventsChannelName;

    private final Short listingEventsChannelReplicationFactor;

    @Min(1)
    @NotNull
    private final Integer listingEventsChannelNumberOfConsumers;

    @Min(1)
    @NotNull
    private final Integer listingEventsChannelNumberOfPartitions;

    @NotBlank
    @NotNull
    private final String internalCommandsChannelPrefix;

    @Min(1)
    @NotNull
    private final Integer internalCommandsChannelNumberOfPartitions;

    private final Short internalCommandsChannelReplicationFactor;

    @Min(1)
    @NotNull
    private final Integer internalCommandsChannelNumberOfConsumers;

    @NotBlank
    public final String publicCommandsChannelPrefix;

    @NotBlank
    public final String publicCommandsGetListingPublicDetailsByIdChannelName;
}
