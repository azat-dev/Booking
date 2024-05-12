package com.azat4dev.demobooking.users.users_commands.application.config;

import com.azat4dev.demobooking.common.DomainEventNew;
import com.azat4dev.demobooking.common.DomainEventPayload;
import com.azat4dev.demobooking.common.DomainEventsBus;
import com.azat4dev.demobooking.common.Policy;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class ConnectPoliciesConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectPoliciesConfig.class);

    @Autowired
    List<Policy<DomainEventNew<?>>> policies;

    @Autowired
    DomainEventsBus domainEventsBus;

    @PostConstruct
    public void connectPoliciesToBus() {
        LOGGER.info("DomainConfig initialized");

        final var groupedPolicies = new HashMap<Class<DomainEventPayload>, List<Policy<DomainEventNew<?>>>>();

        for (final var policy : policies) {

            final var interfaces = policy.getClass().getGenericInterfaces();

            for (final var pi : interfaces) {

                final var policyInterface = (ParameterizedType) pi;

                if (!policyInterface.getRawType().equals(Policy.class)) {
                    continue;
                }

                final var domainEvent = (ParameterizedType) policyInterface.getActualTypeArguments()[0];
                final var payload = ((ParameterizedType) domainEvent).getActualTypeArguments()[0];

                final var items = groupedPolicies.getOrDefault(payload, new LinkedList<>());
                items.add(policy);

                groupedPolicies.put((Class<DomainEventPayload>) payload, items);
            }
        }


        for (var entry : groupedPolicies.entrySet()) {
            final var eventType = entry.getKey();
            final var listeners = entry.getValue();

            for (var listener : listeners) {
                domainEventsBus.listen(eventType, listener::execute);
            }
        }
    }
}
