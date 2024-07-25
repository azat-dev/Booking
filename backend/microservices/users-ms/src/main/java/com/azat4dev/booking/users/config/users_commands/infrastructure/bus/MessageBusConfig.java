package com.azat4dev.booking.users.config.users_commands.infrastructure.bus;

import com.azat4dev.booking.shared.config.infrastracture.bus.KafkaMessageBusConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Import(KafkaMessageBusConfig.class)
@Configuration
public class MessageBusConfig {

}
