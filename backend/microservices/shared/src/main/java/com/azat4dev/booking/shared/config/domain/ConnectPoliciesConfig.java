package com.azat4dev.booking.shared.config.domain;

import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.infrastructure.bus.GetInputTopicForEvent;
import com.azat4dev.booking.shared.infrastructure.bus.MessageListenerForPolicy;
import com.azat4dev.booking.shared.infrastructure.bus.TopicListener;
import com.azat4dev.booking.shared.infrastructure.serializers.MapAnyDomainEvent;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Configuration
public class ConnectPoliciesConfig {

    private List<TopicListener> getPolicyListeners(
        List<Policy<?>> policies,
        GetInputTopicForEvent getInputTopicForEvent,
        MapAnyDomainEvent mapEvent
    ) {

        final var result = new LinkedList<TopicListener>();

        for (final var policy : policies) {

            final var eventType = policy.getEventClass();

            final var listener = new TopicListener(
                getInputTopicForEvent.execute(eventType),
                Optional.of(Set.of(eventType.getSimpleName())),
                new MessageListenerForPolicy(
                    policy,
                    mapEvent::fromDTO
                )
            );

            result.add(listener);
        }

        return result;
    }

    @Bean
    BeanFactoryPostProcessor connectPoliciesToBus(
        List<Policy<?>> policies,
        GetInputTopicForEvent getInputTopicForEvent,
        MapAnyDomainEvent mapEvent
    ) {
        return beanFactory -> {
            final var listeners = getPolicyListeners(
                policies,
                getInputTopicForEvent,
                mapEvent
            );

            for (final var listener : listeners) {
                beanFactory.registerSingleton(
                    "policyTopicListener_" + listener.topic(),
                    listener
                );
            }
        };
    }
}
