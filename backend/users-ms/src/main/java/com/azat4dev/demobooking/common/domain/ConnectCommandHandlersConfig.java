package com.azat4dev.demobooking.common.domain;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Configuration
public class ConnectCommandHandlersConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectCommandHandlersConfig.class);
    private final List<Closeable> cancellations = new LinkedList<>();
    @Autowired
    List<CommandHandler<DomainEventNew<?>>> commandHandlers;
    @Autowired
    DomainEventsBus domainEventsBus;

    private static Map<Class<DomainEventPayload>, List<CommandHandler<DomainEventNew<?>>>> groupHandlersByPayload(List<CommandHandler<DomainEventNew<?>>> handlers) {

        final var groupedHandlers = new HashMap<Class<DomainEventPayload>, List<CommandHandler<DomainEventNew<?>>>>();

        for (final var handler : handlers) {

            final var interfaces = handler.getClass().getGenericInterfaces();

            for (final var pi : interfaces) {

                final var handlerInterface = (ParameterizedType) pi;

                if (!handlerInterface.getRawType().equals(CommandHandler.class)) {
                    continue;
                }

                final var domainEvent = (ParameterizedType) handlerInterface.getActualTypeArguments()[0];
                final var payload = domainEvent.getActualTypeArguments()[0];

                final var items = groupedHandlers.getOrDefault(payload, new LinkedList<>());
                items.add(handler);

                groupedHandlers.put((Class<DomainEventPayload>) payload, items);
            }
        }

        return groupedHandlers;
    }

    @PostConstruct
    public void connectCommandHandlersToBus() {

        final var groupedHandlers = groupHandlersByPayload(commandHandlers);

        for (var entry : groupedHandlers.entrySet()) {
            final var eventType = entry.getKey();
            final var listeners = entry.getValue();

            for (var listener : listeners) {
                final var cancellation = domainEventsBus.listen(eventType, listener::handle);
                cancellations.add(cancellation);
            }
        }

        LOGGER.info("Connected command handlers to the bus {}", commandHandlers);
    }

    @PreDestroy
    public void disconnectHandlersFromBus() {
        cancellations.forEach(cancellation -> {
            try {
                cancellation.close();
            } catch (IOException e) {
                LOGGER.error("Can't close cancellation", e);
            }
        });

        LOGGER.info("Disconnected command handlers from the bus {}");
    }
}
