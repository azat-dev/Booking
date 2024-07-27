package com.azat4dev.booking.shared.config.infrastracture.api;

import com.azat4dev.booking.shared.config.infrastracture.bus.utils.OneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpoint;
import com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpointsContainer;
import com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpointsContainerImpl;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

@Slf4j
@Configuration
@AllArgsConstructor
public class BusApiEndpointsContainerConfig {

    private final ApplicationContext applicationContext;

    @Bean
    BusApiEndpointsContainer busApiEndpointsContainer(
        List<BusApiEndpoint<?>> endpoints,
        MessageBus messageBus,
        OneToOneRelationsOfDtoClassesAndMessageTypes oneToOneRelationsOfDtoClassesAndMessageTypes,
        EventIdGenerator eventIdGenerator
    ) {

        return new BusApiEndpointsContainerImpl(
            endpoints,
            messageBus,
            oneToOneRelationsOfDtoClassesAndMessageTypes::getMessageType,
            () -> eventIdGenerator.generate().toString()
        );
    }

    @PreDestroy
    public void close() {

        final var endpointsContainer = (Closeable) applicationContext.getBean(BusApiEndpointsContainer.class);

        try {
            endpointsContainer.close();
        } catch (IOException e) {
            log.atError()
                .setCause(e)
                .log("Error closing endpoints container: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
