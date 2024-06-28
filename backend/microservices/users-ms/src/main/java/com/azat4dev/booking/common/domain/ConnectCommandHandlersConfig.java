package com.azat4dev.booking.common.domain;

import com.azat4dev.booking.common.domain.annotations.CommandHandlerBean;
import com.azat4dev.booking.shared.domain.CommandHandler;
import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Configuration
public class ConnectCommandHandlersConfig {

    private final List<Closeable> cancellations = new LinkedList<>();
    private final ApplicationContext applicationContext;
    private final DomainEventsBus domainEventsBus;

    private static Map<Class<DomainEventPayload>, List<CommandHandler<Command>>> groupHandlersByPayload(List<CommandHandler<Command>> handlers) {

        final var groupedHandlers = new HashMap<Class<DomainEventPayload>, List<CommandHandler<Command>>>();

        for (final var handler : handlers) {

            final var interfaces = handler.getClass().getGenericInterfaces();

            for (final var pi : interfaces) {

                final var handlerInterface = (ParameterizedType) pi;

                if (!handlerInterface.getRawType().equals(CommandHandler.class)) {
                    continue;
                }

                final var payload = handlerInterface.getActualTypeArguments()[0];

                final var items = groupedHandlers.getOrDefault(payload, new LinkedList<>());
                items.add(handler);

                groupedHandlers.put((Class<DomainEventPayload>) payload, items);
            }
        }

        return groupedHandlers;
    }

    public List<CommandHandler<Command>> findCommandHandlers(ApplicationContext context) {

        List<CommandHandler<Command>> annotatedBeans = new LinkedList<>();
        String[] beanNames = context.getBeanNamesForAnnotation(CommandHandlerBean.class);

        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            if (bean instanceof CommandHandler inst) {
                annotatedBeans.add(inst);
            } else {
                log.error("Bean {} is not a CommandHandler", beanName);
            }
        }
        return annotatedBeans;
    }

    @PostConstruct
    public void connectCommandHandlersToBus() {

        final var commandHandlers = findCommandHandlers(applicationContext);
        final var groupedHandlers = groupHandlersByPayload(commandHandlers);

        for (var entry : groupedHandlers.entrySet()) {
            final var eventType = entry.getKey();
            final var listeners = entry.getValue();

            for (var listener : listeners) {
                final var cancellation = domainEventsBus.listen(
                    eventType,
                    (event) -> {
                        try {
                            listener.handle((Command) event.payload(), event.id(), event.issuedAt());
                        } catch (DomainException e) {
                            throw new RuntimeException(e);
                        }
                    });
                cancellations.add(cancellation);
            }
        }

        log.info("Connected command handlers to the bus {}", commandHandlers);
    }

    @PreDestroy
    public void disconnectHandlersFromBus() {
        cancellations.forEach(cancellation -> {
            try {
                cancellation.close();
            } catch (IOException e) {
                log.error("Can't close cancellation", e);
            }
        });

        log.info("Disconnected command handlers from the bus");
    }
}
