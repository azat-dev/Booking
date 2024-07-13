package com.azat4dev.booking.common.domain;

import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Configuration
public class ConnectPoliciesConfig {

    private final ApplicationContext applicationContext;
    private final DomainEventsBus domainEventsBus;
    private final List<Closeable> cancellations = new LinkedList<>();

    private static Type getEventType(Class<?> clazz) {

        final var handlerClass = ClassUtils.getUserClass(clazz);

        return Arrays.stream(handlerClass.getGenericInterfaces())
            .flatMap(pi -> {
                if (pi instanceof ParameterizedType inst && inst.getRawType().equals(Policy.class)) {
                    return Arrays.stream(inst.getActualTypeArguments());
                }

                return null;
            })
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Can't find event class for " + clazz));
    }

    private static Map<Class<DomainEventPayload>, List<Policy<DomainEventPayload>>> groupPoliciesByEventType(List<Policy<DomainEventPayload>> policies) {

        return policies.stream()
            .collect(
                Collectors.groupingBy(
                    i -> (Class<DomainEventPayload>) getEventType(i.getClass()),
                    Collectors.toList()
                )
            );
    }

    public static List<Policy<DomainEventPayload>> findPolicies(ApplicationContext context) {
        return Arrays.stream(context.getBeanNamesForType(Policy.class))
            .map(name -> (Policy<DomainEventPayload>) context.getBean(name))
            .toList();
    }

    @PostConstruct
    public void connectPoliciesToBus() {

        final var policies = findPolicies(applicationContext);

        log.atInfo()
            .addArgument(() -> policies.stream().map(i -> i.getClass().getSimpleName()).toList())
            .log("Connecting policies to bus: {}");

        final var groupedPolicies = groupPoliciesByEventType(policies);

        for (final var entry : groupedPolicies.entrySet()) {
            final var eventType = entry.getKey();
            final var listeners = entry.getValue();

            for (var listener : listeners) {
                final var policyName = ClassUtils.getUserClass(listener).getSimpleName();
                final var cancellation = domainEventsBus.listen(eventType, event -> {

                    log.atInfo()
                        .addKeyValue("event.id", event::id)
                        .addKeyValue("policy", policyName)
                        .addKeyValue("eventType", eventType)
                        .addArgument(policyName)
                        .addArgument(eventType)
                        .addArgument(event::id)
                        .log("Executing policy: policy={}, eventType={}, event.id={}");

                    try {
                        listener.execute(event.payload(), event.id(), event.issuedAt());
                    } catch (Exception e) {
                        log.atError()
                            .setCause(e)
                            .addKeyValue("event.id", event::id)
                            .addKeyValue("policy", policyName)
                            .addKeyValue("event.type", eventType)
                            .addKeyValue("event.payload", () -> event.payload().toString())
                            .log("Failed to execute policy");
                        throw e;
                    }
                });
                cancellations.add(cancellation);

                log.atInfo()
                    .addArgument(policyName)
                    .addArgument(eventType)
                    .log("Connected policy {} to the bus {}");
            }
        }

        log.atInfo()
            .addArgument(policies.stream().map(Object::getClass).map(Class::getSimpleName).toList())
            .log("Connected policies to the bus: {}");
    }

    @PreDestroy
    public void disconnectPoliciesFromBus() {
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
