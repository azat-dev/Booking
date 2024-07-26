package com.azat4dev.booking.shared.infrastructure.tracing.outbox;

import io.micrometer.observation.Observation.Context;
import io.micrometer.observation.ObservationConvention;

public interface OutboxInputObservationConvention extends ObservationConvention<OutboxInputReceiverContext> {

    @Override
    default boolean supportsContext(Context context) {
        return context instanceof OutboxInputReceiverContext;
    }

    @Override
    default String getName() {
        return "outbox.publisher";
    }
}