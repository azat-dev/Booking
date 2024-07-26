/*
 * Copyright 2022-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azat4dev.booking.shared.infrastructure.tracing.outbox;

import io.micrometer.observation.Observation.Context;
import io.micrometer.observation.ObservationConvention;
import io.micrometer.observation.docs.ObservationDocumentation;

public enum OutboxObservation implements ObservationDocumentation {

    OUTBOX_OUTPUT_OBSERVATION {
        @Override
        public Class<? extends ObservationConvention<? extends Context>> getDefaultConvention() {
            return OutboxOutputObservationConvention.class;
        }
    },

    OUTBOX_INPUT_OBSERVATION {
        @Override
        public Class<? extends ObservationConvention<? extends Context>> getDefaultConvention() {
            return OutboxInputObservationConvention.class;
        }
    };

    public static class DefaultOutboxOutputObservationConvention implements OutboxOutputObservationConvention {

        public static final DefaultOutboxOutputObservationConvention INSTANCE =
            new DefaultOutboxOutputObservationConvention();

    }

    public static class DefaultOutboxInputObservationConvention implements OutboxInputObservationConvention {

        public static final DefaultOutboxInputObservationConvention INSTANCE =
            new DefaultOutboxInputObservationConvention();

    }
}
