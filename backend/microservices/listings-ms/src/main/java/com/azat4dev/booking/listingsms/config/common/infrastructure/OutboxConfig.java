package com.azat4dev.booking.listingsms.config.common.infrastructure;

import com.azat4dev.booking.shared.config.infrastracture.OutboxEventsPublisherConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(OutboxEventsPublisherConfig.class)
@Configuration
public class OutboxConfig {
}
