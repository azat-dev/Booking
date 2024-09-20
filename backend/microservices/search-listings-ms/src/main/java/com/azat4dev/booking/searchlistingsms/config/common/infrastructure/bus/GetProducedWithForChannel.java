package com.azat4dev.booking.searchlistingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.searchlistingsms.generated.api.bus.Channels;
import com.azat4dev.booking.shared.infrastructure.bus.Message;
import org.apache.kafka.streams.kstream.Produced;

@FunctionalInterface
public interface GetProducedWithForChannel {
    Produced<String, Message> run(Channels channel);
}
