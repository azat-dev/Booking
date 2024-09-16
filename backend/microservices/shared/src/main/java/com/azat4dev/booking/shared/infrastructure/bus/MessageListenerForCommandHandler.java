package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.domain.CommandHandler;
import com.azat4dev.booking.shared.domain.events.Command;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.util.function.Function;

@Slf4j
@AllArgsConstructor
public class MessageListenerForCommandHandler implements MessageListener {
    private final CommandHandler handler;
    private final Function<Object, DomainEventPayload> mapMessageToEvent;

    @Override
    public void consume(Message message) {
        final var eventType = message.type();
        final var handlerName = ClassUtils.getUserClass(handler).getSimpleName();
        final var payload = mapMessageToEvent.apply(message.payload());

        final var event = new DomainEvent<>(
            EventId.dangerouslyCreateFrom(message.id()),
            message.sentAt(),
            payload
        );

        log.atInfo()
            .addKeyValue("event.id", event::id)
            .addKeyValue("commandHandler", handlerName)
            .addKeyValue("eventType", eventType)
            .addArgument(handlerName)
            .addArgument(eventType)
            .addArgument(event::id)
            .log("Executing commandHandler: commandHandler={}, eventType={}, event.id={}");

        try {
            handler.handle(event.payload(Command.class), event.id(), event.issuedAt());
        } catch (Exception e) {
            log.atError()
                .setCause(e)
                .addKeyValue("event.id", event::id)
                .addKeyValue("commandHandler", handlerName)
                .addKeyValue("event.type", eventType)
                .addKeyValue("event.payload", () -> event.payload().toString())
                .log("Failed to execute command handler");
            throw new RuntimeException(e);
        }
    }
}