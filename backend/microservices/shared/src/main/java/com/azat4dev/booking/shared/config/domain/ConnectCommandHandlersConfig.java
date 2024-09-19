package com.azat4dev.booking.shared.config.domain;

import com.azat4dev.booking.shared.domain.CommandHandler;
import com.azat4dev.booking.shared.infrastructure.bus.GetInputChannelForEvent;
import com.azat4dev.booking.shared.infrastructure.bus.MessageListenerForCommandHandler;
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
public class ConnectCommandHandlersConfig {

    private List<NewMessageListenerForChannel> getCommandListeners(
        List<CommandHandler> commandHandlers,
        GetInputChannelForEvent getInputTopicForEvent,
        MapAnyDomainEvent mapEvent
    ) {

        final var result = new LinkedList<NewMessageListenerForChannel>();

        for (final var commandHandler : commandHandlers) {

            final var commandType = commandHandler.getCommandClass();

            final var listener = new NewMessageListenerForChannel(
                getInputTopicForEvent.execute(commandType),
                Optional.of(Set.of(commandType.getSimpleName())),
                new MessageListenerForCommandHandler(
                    commandHandler,
                    mapEvent::fromDTO
                )
            );

            result.add(listener);
        }

        return result;
    }

    @Bean
    NewMessageListenersForChannel connectCommandHandlersToBus(
        List<CommandHandler> commandHandlers,
        GetInputChannelForEvent getInputTopicForEvent,
        MapAnyDomainEvent mapEvent
    ) {
        return new NewMessageListenersForChannel(
            getCommandListeners(
                commandHandlers,
                getInputTopicForEvent,
                mapEvent
            )
        );
    }
}
