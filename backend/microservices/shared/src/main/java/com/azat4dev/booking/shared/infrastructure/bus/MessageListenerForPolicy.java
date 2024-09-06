package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.util.function.Function;

@Slf4j
@AllArgsConstructor
public class MessageListenerForPolicy implements MessageListener {
    private final Policy policy;
    private final Function<Object, DomainEventPayload> mapMessageToEvent;

    @Override
    public void consume(Message message) {
        final var eventType = message.type();
        final var policyName = ClassUtils.getUserClass(policy).getSimpleName();
        final var payload = mapMessageToEvent.apply(message.payload());

        final var event = new DomainEvent<>(
            EventId.dangerouslyCreateFrom(message.id()),
            message.sentAt(),
            payload
        );

        log.atInfo()
            .addKeyValue("event.id", event::id)
            .addKeyValue("policy", policyName)
            .addKeyValue("eventType", eventType)
            .addArgument(policyName)
            .addArgument(eventType)
            .addArgument(event::id)
            .log("Executing policy: policy={}, eventType={}, event.id={}");

        try {
            policy.execute(event.payload(), event.id(), event.issuedAt());
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
    }
}