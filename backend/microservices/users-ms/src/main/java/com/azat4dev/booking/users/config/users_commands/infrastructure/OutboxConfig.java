package com.azat4dev.booking.users.config.users_commands.infrastructure;

import com.azat4dev.booking.shared.config.infrastracture.OutboxEventsPublisherConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(OutboxEventsPublisherConfig.class)
@Configuration
public class OutboxConfig {
}
