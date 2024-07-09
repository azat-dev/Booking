package com.azat4dev.booking.common.domain;

import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class ConnectPoliciesConfig {

    private final List<Policy<DomainEvent<?>>> policies;
    private final DomainEventsBus domainEventsBus;
    private final List<Closeable> cancellations = new LinkedList<>();

    private static Map<Class<DomainEventPayload>, List<Policy<DomainEvent<?>>>> groupPolicies(List<Policy<DomainEvent<?>>> policies) {

        log.atDebug()
                .addArgument(() -> policies.stream().map(i -> i.getClass().getSimpleName()).toList())
                .log("Connecting policies to bus: {}");

        final var groupedPolicies = new HashMap<Class<DomainEventPayload>, List<Policy<DomainEvent<?>>>>();

        for (final var policy : policies) {

            final var interfaces = policy.getClass().getGenericInterfaces();

            for (final var pi : interfaces) {

                final var policyInterface = (ParameterizedType) pi;

                if (!policyInterface.getRawType().equals(Policy.class)) {
                    continue;
                }

                final var domainEvent = (ParameterizedType) policyInterface.getActualTypeArguments()[0];
                final var payload = domainEvent.getActualTypeArguments()[0];

                final var items = groupedPolicies.getOrDefault(payload, new LinkedList<>());
                items.add(policy);

                groupedPolicies.put((Class<DomainEventPayload>) payload, items);
            }
        }

        return groupedPolicies;
    }

    @PostConstruct
    public void connectPoliciesToBus() {

        final var groupedPolicies = groupPolicies(policies);

        for (final var entry : groupedPolicies.entrySet()) {
            final var eventType = entry.getKey();
            final var listeners = entry.getValue();

            for (var listener : listeners) {
                final var cancellation = domainEventsBus.listen(eventType, (event) -> {

                    log.atDebug()
                            .addKeyValue("event.id", event::id)
                            .addKeyValue("policy", () -> listener.getClass().getSimpleName())
                            .addKeyValue("eventType", eventType)
                            .log("Executing policy");

                    try {
                        listener.execute(event);
                    } catch (Exception e) {
                        log.atError()
                                .setCause(e)
                                .addKeyValue("event.id", event::id)
                                .addKeyValue("policy", () -> listener.getClass().getSimpleName())
                                .addKeyValue("event.type", eventType)
                                .addKeyValue("event.payload", () -> event.payload().toString())
                                .log("Failed to execute policy");
                        throw e;
                    }
                });
                cancellations.add(cancellation);
            }
        }

        log.atInfo()
                .addArgument(policies.stream().map(Object::getClass).map(Class::getSimpleName).toList())
                .log("Connected policies to the bus: {}");
    }

    @PreDestroy
    public void disconnectHandlersFromBus() {
        cancellations.forEach(cancellation -> {
            try {
                cancellation.close();
            } catch (IOException e) {
                log.atError()
                        .setCause(e)
                        .log("Can't close cancellation");
            }
        });

        log.atInfo()
                .log("Disconnected policies from the bus");
    }
}
