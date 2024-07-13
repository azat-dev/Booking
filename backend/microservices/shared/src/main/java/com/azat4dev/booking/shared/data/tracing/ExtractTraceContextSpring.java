package com.azat4dev.booking.shared.data.tracing;

import com.azat4dev.booking.shared.data.tracing.outbox.OutboxInputReceiverContext;
import com.azat4dev.booking.shared.data.tracing.outbox.OutboxObservation;
import com.azat4dev.booking.shared.domain.interfaces.tracing.ExtractTraceContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
@AllArgsConstructor
public class ExtractTraceContextSpring implements ExtractTraceContext {

    private final ObjectMapper objectMapper;
    private final ObservationRegistry observationRegistry;

    @Override
    public String execute() {

        final var tracingInfo = new HashMap<String, String>();

        Observation observation = OutboxObservation.OUTBOX_INPUT_OBSERVATION.observation(
            null,
            OutboxObservation.DefaultOutboxInputObservationConvention.INSTANCE,
            () -> new OutboxInputReceiverContext(tracingInfo),
            this.observationRegistry
        );

        try {
            observation.start();
            try (Observation.Scope ignored = observation.openScope()) {
                return objectMapper.writeValueAsString(tracingInfo);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException ex) {
            // The error is added from org.apache.kafka.clients.producer.Callback
            if (observation.getContext().getError() == null) {
                observation.error(ex);
                observation.stop();
            }
            throw ex;
        }
    }
}
