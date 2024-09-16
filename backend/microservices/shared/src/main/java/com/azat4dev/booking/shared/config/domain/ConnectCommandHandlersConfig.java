package com.azat4dev.booking.shared.config.domain;

import com.azat4dev.booking.shared.domain.CommandHandler;
import com.azat4dev.booking.shared.infrastructure.bus.GetInputTopicForEvent;
import com.azat4dev.booking.shared.infrastructure.bus.MessageListenerForCommandHandler;
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
public class ConnectCommandHandlersConfig {

    private List<NewTopicMessageListener> getCommandListeners(
        List<CommandHandler> commandHandlers,
        GetInputTopicForEvent getInputTopicForEvent,
        MapAnyDomainEvent mapEvent
    ) {

        final var result = new LinkedList<NewTopicMessageListener>();

        for (final var commandHandler : commandHandlers) {

            final var commandType = commandHandler.getCommandClass();

            final var listener = new NewTopicMessageListener(
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
    NewTopicListeners connectCommandHandlersToBus(
        List<CommandHandler> commandHandlers,
        GetInputTopicForEvent getInputTopicForEvent,
        MapAnyDomainEvent mapEvent
    ) {
        return new NewTopicListeners(
            getCommandListeners(
                commandHandlers,
                getInputTopicForEvent,
                mapEvent
            )
        );
    }
}
