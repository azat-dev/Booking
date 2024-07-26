package com.azat4dev.booking.shared.infrastructure.tracing;

import com.azat4dev.booking.shared.infrastructure.tracing.outbox.OutboxObservation;
import com.azat4dev.booking.shared.infrastructure.tracing.outbox.OutboxOutputReceiverContext;
import com.azat4dev.booking.shared.domain.interfaces.tracing.ExecuteWithTraceContext;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class ExecuteWithTraceContextSpring implements ExecuteWithTraceContext {

    private final ObservationRegistry observationRegistry;

    @Override
    public void run(@Nullable Map<String, String> traceInfo, Runnable action) {

        if (traceInfo == null) {
            action.run();
            return;
        }

        Observation observation = OutboxObservation.OUTBOX_OUTPUT_OBSERVATION.observation(
            null,
            OutboxObservation.DefaultOutboxOutputObservationConvention.INSTANCE,
            () -> new OutboxOutputReceiverContext(traceInfo),
            this.observationRegistry);

        observation.observe(action);
    }
}
