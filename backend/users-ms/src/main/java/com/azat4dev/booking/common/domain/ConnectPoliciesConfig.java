package com.azat4dev.booking.common.domain;

import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Configuration
public class ConnectPoliciesConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectPoliciesConfig.class);

    @Autowired
    List<Policy<DomainEvent<?>>> policies;

    @Autowired
    DomainEventsBus domainEventsBus;

    List<Closeable> cancellations = new LinkedList<>();

    private static Map<Class<DomainEventPayload>, List<Policy<DomainEvent<?>>>> groupPolicies(List<Policy<DomainEvent<?>>> policies) {

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
                final var cancellation = domainEventsBus.listen(eventType, listener::execute);
                cancellations.add(cancellation);
            }
        }

        LOGGER.info("Connected {} policies to the bus",
            policies.stream().map(Object::getClass).map(Class::getSimpleName).toList());
    }
}
