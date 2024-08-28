package com.azat4dev.booking.searchlistingsms.config.common.infrastructure.bus;


import com.azat4dev.booking.searchlistingsms.config.common.properties.BusProperties;
import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultMessageBusConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties(BusProperties.class)
@Import(DefaultMessageBusConfig.class)
@Configuration
public class MessageBusConfig {

}

