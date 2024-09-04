package com.azat4dev.booking.shared.config.infrastracture.bus;

import com.azat4dev.booking.shared.infrastructure.bus.DefaultMakeTopologyForTopic;
import com.azat4dev.booking.shared.infrastructure.bus.Message;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    KafkaMessageBusConfig.class,
    CombineOneToOneRelationsOfDtoClassesAndMessageTypesConfig.class,
    CollectOneToOneRelationsOfDtoClassesAndMessageTypesForDomainEventsConfig.class,
    DefaultMapAnyDomainEventConfig.class
})
@Configuration
public class DefaultMessageBusConfig {

    @Bean
    DefaultMakeTopologyForTopic defaultMakeTopologyForTopic(Serde<Message> serde) {
        return new DefaultMakeTopologyForTopic(serde);
    }
}
