package com.azat4dev.booking.searchlistingsms.config.common.infrastructure.bus;

import com.azat4dev.booking.shared.infrastructure.bus.kafka.GetSerdeForTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetSerdeForChannelConfig {

    @Bean
    GetConsumedWithForChannel consumedWith(GetSerdeForChannel getSerdeForChannel) {
        return (channel) -> Consumed.with(
            Serdes.String(),
            getSerdeForChannel.run(channel)
        );
    }

    @Bean
    GetProducedWithForChannel producedWith(GetSerdeForChannel getSerdeForChannel) {
        return channel -> Produced.with(
            Serdes.String(),
            getSerdeForChannel.run(channel)
        );
    }

    @Bean
    GetSerdeForChannel getSerdeForChannel(GetSerdeForTopic getSerdeForTopic) {
        return channel -> getSerdeForTopic.run(channel.getValue());
    }
}
