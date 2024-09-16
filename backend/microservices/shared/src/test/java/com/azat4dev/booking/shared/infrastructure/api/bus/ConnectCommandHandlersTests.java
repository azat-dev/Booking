package com.azat4dev.booking.shared.infrastructure.api.bus;


import com.azat4dev.booking.shared.config.domain.ConnectCommandHandlersConfig;
import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultDomainEventsBusConfig;
import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultMessageBusConfig;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.OneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.serializers.DefaultTimeSerializerConfig;
import com.azat4dev.booking.shared.config.infrastracture.services.DefaultTimeProviderConfig;
import com.azat4dev.booking.shared.domain.CommandHandler;
import com.azat4dev.booking.shared.domain.events.Command;
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
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
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
@EnableTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ConnectCommandHandlersTests {

    @Autowired
    KafkaProperties kafkaProperties;

    @Test
    public void test_kafka() {

        System.out.println(kafkaProperties);
    }

    private static final String SIMPLE_TOPIC = "simple_topic";

    @Qualifier("commandHandlerCallback")
    @Autowired
    AtomicReference<Consumer<TestCommand>> commandHandlerCallback;

    @Autowired
    DomainEventsBus bus;

    @BeforeEach
    void setUp() {
        commandHandlerCallback.set(null);
    }

    @Autowired
    MessageBus messageBus;

    private String anyDomainEventId() {
        return UUID.randomUUID().toString();
    }

    private TestCommand anyTestEvent() {
        return new TestCommand(anyDomainEventId());
    }

    @Test
    void test_publish_givenCommandPublished_thenCommandHandlerMustBeTriggered() {

        // Given
        final var event = anyTestEvent();

        final var receivedEventStore = new AtomicReference<TestCommand>();
        commandHandlerCallback.set(receivedEventStore::set);

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

    public record TestCommand(String value) implements Command {
    }

    @TestConfiguration
    @Import({
        DefaultMessageBusConfig.class,
        DefaultTimeProviderConfig.class,
        DefaultDomainEventsBusConfig.class,
        DefaultTimeSerializerConfig.class,
        ConnectCommandHandlersConfig.class
    })
    public static class TestConfig {

        @Bean("commandHandlerCallback")
        AtomicReference<Consumer<TestCommand>> commandHandlerCallback() {
            return new AtomicReference<>();
        }

        @Bean
        MapDomainEvent<TestCommand, TestMessageDTO> mapDomainEvent() {
            return new MapDomainEvent<TestCommand, TestMessageDTO>() {
                @Override
                public TestMessageDTO serialize(TestCommand domain) {
                    return new TestMessageDTO(domain.value(), "payload");
                }

                @Override
                public TestCommand deserialize(TestMessageDTO dto) {
                    return new TestCommand(dto.getId().toString());
                }

                @Override
                public Class<TestCommand> getOriginalClass() {
                    return TestCommand.class;
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
                new OneToOneRelationsOfDtoClassesAndMessageTypes.Item("TestCommand", TestMessageDTO.class)
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
        CommandHandler<TestCommand> testCommandHandler() {
            return new CommandHandler<>() {
                @Override
                public void handle(TestCommand command, EventId eventId, LocalDateTime issuedAt) {
                    commandHandlerCallback().get().accept(command);
                }

                @Override
                public Class<TestCommand> getCommandClass() {
                    return TestCommand.class;
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
            return message -> {
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
                    throw new MessageSerializer.Exception.FailedSerialize(e);
                }
            };
        }

        @Bean
        MessageDeserializer messageDeserializer() {
            return serializedMessage -> {
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
                    throw new MessageDeserializer.Exception.FailedDeserialize(e);
                }
            };
        }
    }
}
