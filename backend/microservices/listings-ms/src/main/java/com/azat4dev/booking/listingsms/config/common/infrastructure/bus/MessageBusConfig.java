package com.azat4dev.booking.listingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.listingsms.config.common.properties.BusProperties;
import com.azat4dev.booking.shared.config.infrastracture.bus.KafkaMessageBusConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties(BusProperties.class)
@Import(KafkaMessageBusConfig.class)
@Configuration
public class MessageBusConfig {

}
