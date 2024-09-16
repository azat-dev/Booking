package com.azat4dev.booking.shared.config.domain;

import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.infrastructure.bus.GetInputTopicForEvent;
import com.azat4dev.booking.shared.infrastructure.bus.MessageListenerForPolicy;
import com.azat4dev.booking.shared.infrastructure.bus.NewTopicMessageListener;
import com.azat4dev.booking.shared.infrastructure.bus.NewTopicListeners;
import com.azat4dev.booking.shared.infrastructure.serializers.MapAnyDomainEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Configuration
public class ConnectPoliciesConfig {

    private List<NewTopicMessageListener> getPolicyListeners(
        List<Policy<?>> policies,
        GetInputTopicForEvent getInputTopicForEvent,
        MapAnyDomainEvent mapEvent
    ) {

        final var result = new LinkedList<NewTopicMessageListener>();

        for (final var policy : policies) {

            final var eventType = policy.getEventClass();

            final var listener = new NewTopicMessageListener(
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
    NewTopicListeners connectPoliciesToBus(
        List<Policy<?>> policies,
        GetInputTopicForEvent getInputTopicForEvent,
        MapAnyDomainEvent mapEvent
    ) {

        return new NewTopicListeners(
            getPolicyListeners(
                policies,
                getInputTopicForEvent,
                mapEvent
            )
        );
    }
}
