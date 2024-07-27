package com.azat4dev.booking.shared.config.infrastracture.bus;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    KafkaMessageBusConfig.class,
    CombineOneToOneRelationsOfDtoClassesAndMessageTypesConfig.class,
    CollectOneToOneRelationsOfDtoClassesAndMessageTypesForDomainEventsConfig.class,
    JsonMessageBusSerializerConfig.class,
    DefaultMapAnyDomainEventConfig.class
})
@Configuration
public class DefaultMessageBusConfig {
}
