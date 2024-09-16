package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.kafka.GetSerdeForTopic;
import com.azat4dev.booking.shared.infrastructure.bus.kafka.SerdesForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageDeserializersForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageSerializersForTopics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    KafkaMessageBusConfig.class,
    CombineRelationsOfDtoClassesAndMessageTypesConfig.class,
    CollectRelationsOfDtoClassesAndMessageTypesForDomainEventsConfig.class,
    DefaultMapAnyDomainEventConfig.class
})
@Configuration
public class DefaultMessageBusConfig {

    @Bean
    GetSerdeForTopic getSerdeForTopic(
        MessageSerializersForTopics serializers,
        MessageDeserializersForTopics deserializers
    ) {
        final var serdes = new SerdesForTopics(serializers, deserializers);

        return serdes::getForTopic;
    }
}
