package com.azat4dev.booking.shared.config.infrastracture.api;

import com.azat4dev.booking.shared.config.infrastracture.bus.utils.RelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpoint;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.MessageListenerForBusApiEndpoint;
import com.azat4dev.booking.shared.infrastructure.bus.NewMessageListenerForChannel;
import com.azat4dev.booking.shared.infrastructure.bus.NewMessageListenersForChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(BusApiEndpointsItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypesConfig.class)
@Configuration
public class ConnectBusApiEndpointsConfig {

    volatile MessageBus messageBus;

    @Bean
    public BeanPostProcessor saveMessageBusToRef() {

        final var ref = this;

        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof MessageBus messageBus) {
                    ref.messageBus = messageBus;
                }

                return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
            }
        };
    }

    @Bean
    NewMessageListenersForChannel topicListenersForBusApiEndpoints(
        List<BusApiEndpoint<?>> endpoints,
        RelationsOfDtoClassesAndMessageTypes oneToOneRelationsOfDtoClassesAndMessageTypes,
        EventIdGenerator eventIdGenerator
    ) {

        return new NewMessageListenersForChannel(
            endpoints.stream()
                .map(endpoint -> new NewMessageListenerForChannel(
                    endpoint.getInputAddress(),
                    new MessageListenerForBusApiEndpoint(
                        endpoint,
                        () -> this.messageBus,
                        () -> eventIdGenerator.generate().toString(),
                        oneToOneRelationsOfDtoClassesAndMessageTypes::getMessageType
                    )
                )).toList()
        );
    }
}
