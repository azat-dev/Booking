package com.azat4dev.booking.shared.infrastructure.api.bus;


import com.azat4dev.booking.shared.config.domain.ConnectPoliciesConfig;
import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultDomainEventsBusConfig;
import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultMessageBusConfig;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.OneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.serializers.DefaultTimeSerializerConfig;
import com.azat4dev.booking.shared.config.infrastracture.services.DefaultTimeProviderConfig;
import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.generated.dto.MessageDTO;
import com.azat4dev.booking.shared.generated.dto.TestMessageDTO;
import com.azat4dev.booking.shared.helpers.EnableTestcontainers;
import com.azat4dev.booking.shared.infrastructure.bus.*;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.CustomMessageDeserializerForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.CustomMessageSerializerForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageDeserializer;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageSerializer;
import com.azat4dev.booking.shared.infrastructure.serializers.MapDomainEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.azat4dev.booking.shared.helpers.Helpers.waitForValue;
import static org.assertj.core.api.Assertions.assertThat;

@EnableKafka
@Import({
    DefaultDomainEventsBusConfig.class,
    DefaultMessageBusConfig.class,
    DefaultTimeProviderConfig.class,
    DefaultTimeSerializerConfig.class,
    ConnectPoliciesConfig.class
})
@EnableTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ConnectPoliciesTests {

    private static final String SIMPLE_TOPIC = "simple_topic";

    @Qualifier("policyCallback")
    @Autowired
    AtomicReference<Consumer<TestDomainEvent>> policyCallback;

    @Autowired
    DomainEventsBus bus;

    @BeforeEach
    void setUp() {
        policyCallback.set(null);
    }

    @Autowired
    MessageBus messageBus;

    private String anyDomainEventId() {
        return UUID.randomUUID().toString();
    }

    private TestDomainEvent anyTestEvent() {
        return new TestDomainEvent(anyDomainEventId());
    }

    @Test
    void test_publish_givenDomainEventPublished_thenPolicyMustReceiveEvent() {

        // Given
        final var event = anyTestEvent();

        final var receivedEventStore = new AtomicReference<TestDomainEvent>();
        policyCallback.set(receivedEventStore::set);

        // When
        bus.publish(event);

        // Then
        waitForValue(receivedEventStore, Duration.ofSeconds(1));

        final var receivedEvent = receivedEventStore.get();
        assertThat(receivedEvent).isEqualTo(event);
    }

    @SpringBootApplication
    public static class TestApp {

        public static void main(String[] args) {
            SpringApplication.run(TestApp.class, args);
        }
    }

    public record TestDomainEvent(String value) implements DomainEventPayload {

    }

    @TestConfiguration
    public static class TestConfig {


        @Bean("policyCallback")
        AtomicReference<Consumer<TestDomainEvent>> policyCallback() {
            return new AtomicReference<>();
        }

        @Bean
        MapDomainEvent<TestDomainEvent, TestMessageDTO> mapDomainEvent() {
            return new MapDomainEvent<TestDomainEvent, TestMessageDTO>() {
                @Override
                public TestMessageDTO serialize(TestDomainEvent domain) {
                    return new TestMessageDTO(domain.value(), "payload");
                }

                @Override
                public TestDomainEvent deserialize(TestMessageDTO dto) {
                    return new TestDomainEvent(dto.getId().toString());
                }

                @Override
                public Class<TestDomainEvent> getOriginalClass() {
                    return TestDomainEvent.class;
                }

                @Override
                public Class<TestMessageDTO> getSerializedClass() {
                    return TestMessageDTO.class;
                }
            };
        }

        @Bean
        OneToOneRelationsOfDtoClassesAndMessageTypes oneToOneRelationsOfDtoClassesAndMessageTypes() {
            return new OneToOneRelationsOfDtoClassesAndMessageTypes(
                new OneToOneRelationsOfDtoClassesAndMessageTypes.Item("TestDomainEvent", TestMessageDTO.class)
            );
        }

        @Bean
        GetInputTopicForEvent getInputTopicForEvent() {
            return eventType -> SIMPLE_TOPIC;
        }

        @Bean
        GetOutputTopicForEvent getOutputTopicForEvent() {
            return eventType -> SIMPLE_TOPIC;
        }

        @Bean
        GetPartitionKeyForEvent getPartitionKeyForEvent() {
            return new GetPartitionKeyForEvent() {
                @Override
                public <T extends DomainEventPayload> Optional<String> execute(T event) {
                    return Optional.empty();
                }
            };
        }

        @Bean
        Policy<TestDomainEvent> testPolicy() {
            return new Policy<>() {
                @Override
                public void execute(TestDomainEvent event, EventId eventId, LocalDateTime issuedAt) {
                    policyCallback().get().accept(event);
                }

                @Override
                public Class<TestDomainEvent> getEventClass() {
                    return TestDomainEvent.class;
                }
            };
        }

        @Bean
        KafkaAdmin.NewTopics newTopicList() {
            return new KafkaAdmin.NewTopics(
                new NewTopic(SIMPLE_TOPIC, 1, (short) 1)
            );
        }

        @Bean
        CustomMessageSerializerForTopics messageSerializerForInputTopic1(MessageSerializer serializer) {
            return new CustomMessageSerializerForTopics(
                List.of(SIMPLE_TOPIC),
                serializer
            );
        }

        @Bean
        CustomMessageDeserializerForTopics messageDeserializerForTopics(MessageDeserializer deserializer) {
            return new CustomMessageDeserializerForTopics(
                List.of(SIMPLE_TOPIC),
                deserializer
            );
        }

        @Bean
        MessageSerializer messageSerializer() {
            return new MessageSerializer() {

                @Override
                public byte[] serialize(Message message) throws Exception.FailedSerialize {
                    try {

                        final var dto = new MessageDTO(
                            message.id(),
                            message.type(),
                            message.correlationId().orElse(null),
                            message.replyTo().orElse(null),
                            message.payload()
                        );
                        return MessageDTO.getEncoder().encode(dto).array();
                    } catch (IOException e) {
                        throw new Exception.FailedSerialize(e);
                    }
                }
            };
        }

        @Bean
        MessageDeserializer messageDeserializer() {
            return new MessageDeserializer() {

                @Override
                public Message deserialize(byte[] serializedMessage) throws MessageSerializer.Exception {
                    try {
                        final var dto = MessageDTO.getDecoder().decode(serializedMessage);

                        return new Message(
                            dto.getId().toString(),
                            dto.getType().toString(),
                            LocalDateTime.now(),
                            Optional.ofNullable(dto.getCorrelationId()).map(CharSequence::toString),
                            Optional.ofNullable(dto.getReplyToId()).map(CharSequence::toString),
                            dto.getPayload()
                        );
                    } catch (IOException e) {
                        throw new Exception.FailedDeserialize(e);
                    }
                }
            };
        }
    }
}
