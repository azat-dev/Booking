package com.azat4dev.booking.shared.config.domain;

import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.infrastructure.bus.GetInputChannelForEvent;
import com.azat4dev.booking.shared.infrastructure.bus.MessageListenerForPolicy;
import com.azat4dev.booking.shared.infrastructure.bus.NewMessageListenerForChannel;
import com.azat4dev.booking.shared.infrastructure.bus.NewMessageListenersForChannel;
import com.azat4dev.booking.shared.infrastructure.serializers.MapAnyDomainEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Configuration
public class ConnectPoliciesConfig {

    private List<NewMessageListenerForChannel> getPolicyListeners(
        List<Policy<?>> policies,
        GetInputChannelForEvent getInputTopicForEvent,
        MapAnyDomainEvent mapEvent
    ) {

        final var result = new LinkedList<NewMessageListenerForChannel>();

        for (final var policy : policies) {

            final var eventType = policy.getEventClass();

            final var listener = new NewMessageListenerForChannel(
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
    NewMessageListenersForChannel connectPoliciesToBus(
        List<Policy<?>> policies,
        GetInputChannelForEvent getInputTopicForEvent,
        MapAnyDomainEvent mapEvent
    ) {

        return new NewMessageListenersForChannel(
            getPolicyListeners(
                policies,
                getInputTopicForEvent,
                mapEvent
            )
        );
    }
}
