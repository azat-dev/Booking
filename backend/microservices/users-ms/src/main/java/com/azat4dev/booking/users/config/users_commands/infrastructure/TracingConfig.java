package com.azat4dev.booking.users.config.users_commands.infrastructure;

import com.azat4dev.booking.shared.config.infrastracture.DefaultTracingConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(DefaultTracingConfig.class)
@Configuration
public class TracingConfig {
}
