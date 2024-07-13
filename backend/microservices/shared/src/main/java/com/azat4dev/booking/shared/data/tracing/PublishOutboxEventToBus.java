package com.azat4dev.booking.shared.data.tracing;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.interfaces.tracing.ExecuteWithTraceContext;
import com.azat4dev.booking.shared.domain.producers.PublishOutboxEvent;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Observed
@AllArgsConstructor
public class PublishOutboxEventToBus implements PublishOutboxEvent {

    private final ParseTracingInfo parseTracingInfo;
    private final DomainEventsBus bus;
    private final ExecuteWithTraceContext executeWithTraceContext;

    @Override
    public void execute(DomainEventPayload event, LocalDateTime issuedAt, EventId eventId, String tracingInfo) {
        try {
            final var traceInfo = parseTracingInfo.execute(tracingInfo);
            executeWithTraceContext.run(
                traceInfo,
                () -> bus.publish(event, issuedAt, eventId)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
