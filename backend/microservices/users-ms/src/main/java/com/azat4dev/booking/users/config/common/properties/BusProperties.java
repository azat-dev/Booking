package com.azat4dev.booking.users.config.common.properties;

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
    private final String userEventsTopicName;

    @Min(1)
    private final Integer userEventsTopicPartitions;
    private final Short userEventsTopicReplicationFactor;
    private final Integer userEventsTopicNumberOfConsumers;

    @NotBlank
    @NotNull
    private final String internalCommandsTopicPrefix;

    @Min(1)
    private final Integer internalCommandsTopicPartitions;
    private final Short internalCommandsTopicReplicationFactor;
    private final Integer internalCommandsTopicNumberOfConsumers;
}
