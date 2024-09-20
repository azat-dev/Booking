package com.azat4dev.booking.searchlistingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.searchlistingsms.generated.api.bus.Channels;
import com.azat4dev.booking.shared.infrastructure.bus.Message;
import org.apache.kafka.streams.kstream.Consumed;

@FunctionalInterface
public interface GetConsumedWithForChannel {
    Consumed<String, Message> run(Channels channel);
}
