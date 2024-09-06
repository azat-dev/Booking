package com.azat4dev.booking.shared.config.domain;

import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.infrastructure.bus.GetInputTopicForEvent;
import com.azat4dev.booking.shared.infrastructure.bus.TopicListener;
import com.azat4dev.booking.shared.infrastructure.serializers.MapAnyDomainEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Configuration
public class ConnectPoliciesConfig {

//    private final List<Policy<?>> policies;
//    private final MapAnyDomainEvent mapEvent;
//    private final GetInputTopicForEvent getInputTopicForEvent;
//
//    private static Map<Class<DomainEventPayload>, List<Policy<DomainEventPayload>>> groupPoliciesByEventType(List<Policy<DomainEventPayload>> policies) {
//
//        return policies.stream()
//            .collect(
//                Collectors.groupingBy(
//                    Policy::getEventClass,
//                    Collectors.toList()
//                )
//            );
//    }
//
//    private List<TopicListener> getPolicyListeners() {
//
//        final var result = new LinkedList<TopicListener>();
//
//        for (final var policy : policies) {
//
//            final var eventType = policy.getEventClass();
//
//            final var listener = new TopicListener(
//                getInputTopicForEvent.execute(eventType),
//                Set.of(eventType.getSimpleName()),
//                message -> {
//
//                    final var policyName = ClassUtils.getUserClass(policy).getSimpleName();
//                    final var payload = mapEvent.fromDTO(message.payload());
//
//                    final var event = new DomainEvent<>(
//                        EventId.dangerouslyCreateFrom(message.id()),
//                        message.sentAt(),
//                        payload
//                    );
//
//                    log.atInfo()
//                        .addKeyValue("event.id", event::id)
//                        .addKeyValue("policy", policyName)
//                        .addKeyValue("eventType", eventType)
//                        .addArgument(policyName)
//                        .addArgument(eventType)
//                        .addArgument(event::id)
//                        .log("Executing policy: policy={}, eventType={}, event.id={}");
//
//                    try {
//                        policy.execute(event.payload(), event.id(), event.issuedAt());
//                    } catch (Exception e) {
//                        log.atError()
//                            .setCause(e)
//                            .addKeyValue("event.id", event::id)
//                            .addKeyValue("policy", policyName)
//                            .addKeyValue("event.type", eventType)
//                            .addKeyValue("event.payload", () -> event.payload().toString())
//                            .log("Failed to execute policy");
//                        throw e;
//                    }
//                }
//            );
//
//            result.add(listener);
//        }
//
//        return result;
//    }
//
//    @Bean
//    BeanFactoryPostProcessor connectPoliciesToBus() {
//        return beanFactory -> {
//            final var listeners = getPolicyListeners();
//
//            for (final var listener : listeners) {
//                beanFactory.registerSingleton(
//                    "policyTopicListener_" + listener.topic(),
//                    listener
//                );
//            }
//        };
//    }
}
