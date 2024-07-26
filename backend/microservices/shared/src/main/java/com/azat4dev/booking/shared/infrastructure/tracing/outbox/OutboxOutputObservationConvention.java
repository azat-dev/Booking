package com.azat4dev.booking.shared.infrastructure.tracing.outbox;

import io.micrometer.observation.Observation.Context;
import io.micrometer.observation.ObservationConvention;

public interface OutboxOutputObservationConvention extends ObservationConvention<OutboxOutputReceiverContext> {

    @Override
    default boolean supportsContext(Context context) {
        return context instanceof OutboxOutputReceiverContext;
    }

    @Override
    default String getName() {
        return "outbox.publisher";
    }

}