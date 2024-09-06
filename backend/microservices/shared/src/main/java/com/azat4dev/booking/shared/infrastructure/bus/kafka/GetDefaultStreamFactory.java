package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;

@AllArgsConstructor
public class GetDefaultStreamFactory {

    private final GetSerdeForTopic getSerdeForTopic;

    public DefaultStreamFactory run(String topic) {
        return new DefaultStreamFactory(topic, Serdes.String(), getSerdeForTopic.run(topic));
    }
}
