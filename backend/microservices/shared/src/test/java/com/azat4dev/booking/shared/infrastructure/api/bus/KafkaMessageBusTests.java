package com.azat4dev.booking.shared.infrastructure.api.bus;


import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultMessageBusConfig;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.OneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.serializers.DefaultTimeSerializerConfig;
import com.azat4dev.booking.shared.config.infrastracture.services.DefaultTimeProviderConfig;
import com.azat4dev.booking.shared.generated.dto.JoinedMessage;
import com.azat4dev.booking.shared.generated.dto.JoinedMessageDTO;
import com.azat4dev.booking.shared.generated.dto.MessageDTO;
import com.azat4dev.booking.shared.generated.dto.TestMessageDTO;
import com.azat4dev.booking.shared.helpers.EnableTestcontainers;
import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.kafka.CustomTopologyForTopic;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.CustomMessageDeserializerForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.CustomMessageSerializerForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageDeserializer;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@EnableKafka
@Import({
    DefaultMessageBusConfig.class,
    DefaultTimeProviderConfig.class,
    DefaultTimeSerializerConfig.class
})
@EnableTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class KafkaMessageBusTests {

    private static final String SIMPLE_TOPIC = "test_" + UUID.randomUUID().toString();
    private static final String TOPIC_WITH_JOIN = "test_join_" + UUID.randomUUID().toString();
    private static final String INPUT_TOPIC1 = "input1_" + UUID.randomUUID().toString();
    private static final String INPUT_TOPIC2 = "input2_" + UUID.randomUUID().toString();

    @Autowired
    MessageBus messageBus;

    @Test
    void test_publish_givenValidMessage_thenReceive() {

        // Given
        final var topic = SIMPLE_TOPIC;
        final var messageId = UUID.randomUUID().toString();
        final var messageType = "TestMessage";
        final var message = new TestMessageDTO("id", "payload");

        // When
        messageBus.publish(
            topic,
            Optional.empty(),
            MessageBus.Data.with(
                messageId,
                messageType,
                message
            )
        );

        // Then
        final var receivedMessage = waitForResult((Consumer<Message> completed) -> {
            messageBus.listen(topic, msg -> {
                completed.accept(msg);
            });
        });

        assertThat(receivedMessage.payload()).isEqualTo(message);
    }

    @Test
    void test_publish_givenTwoInputTopics_thenJoin() {

        // Given
        final var messageId1 = UUID.randomUUID().toString();
        final var messageId2 = UUID.randomUUID().toString();
        final var messageType = "TestMessage";

        // When
        messageBus.publish(
            INPUT_TOPIC1,
            Optional.empty(),
            MessageBus.Data.with(
                messageId1,
                messageType,
                new TestMessageDTO(messageId1, "payload")
            )
        );

        messageBus.publish(
            INPUT_TOPIC2,
            Optional.empty(),
            MessageBus.Data.with(
                messageId2,
                messageType,
                new TestMessageDTO(messageId2, "payload")
            )
        );

        // Then
        final var expected = new JoinedMessage(messageId1, messageId2);

        final var receivedMessage = waitForResult((Consumer<Message> completed) -> {
            messageBus.listen(TOPIC_WITH_JOIN, msg -> {
                completed.accept(msg);
            });
        });

        assertThat(receivedMessage.payload()).isEqualTo(expected);
    }

    static <T> T waitForResult(Consumer<Consumer<T>> action) {
        final var completed = new AtomicBoolean(false);
        final var result = new AtomicReference<T>();

        action.accept((value) -> {
            result.set(value);
            completed.set(true);
        });
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilTrue(completed);
        return result.get();
    }

    @SpringBootApplication
    public static class TestApp {

        public static void main(String[] args) {
            SpringApplication.run(TestApp.class, args);
        }
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        KafkaAdmin.NewTopics newTopicList() {
            return new KafkaAdmin.NewTopics(
                new NewTopic(SIMPLE_TOPIC, 1, (short) 1),
                new NewTopic(TOPIC_WITH_JOIN, 1, (short) 1)
            );
        }

        @Bean
        ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes getDtoClassForMessageType() {
            return new ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes(
                new OneToOneRelationsOfDtoClassesAndMessageTypes.Item("TestMessage", TestMessageDTO.class),
                new OneToOneRelationsOfDtoClassesAndMessageTypes.Item("JoinedMessage", JoinedMessageDTO.class)
            );
        }

        @Bean
        Serde<JoinedMessage> joinedMessageSerde(ObjectMapper objectMapper) {
            return new Serde<JoinedMessage>() {
                @Override
                public Serializer<JoinedMessage> serializer() {
                    return new Serializer<JoinedMessage>() {
                        @Override
                        public byte[] serialize(String s, JoinedMessage joinedMessage) {
                            try {
                                return objectMapper.writeValueAsBytes(joinedMessage);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };
                }

                @Override
                public Deserializer<JoinedMessage> deserializer() {
                    return new Deserializer<JoinedMessage>() {
                        @Override
                        public JoinedMessage deserialize(String s, byte[] bytes) {
                            try {
                                return objectMapper.readValue(bytes, JoinedMessage.class);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };
                }
            };
        }

        @Bean
        CustomMessageSerializerForTopics messageSerializerForInputTopic1(MessageSerializer serializer) {
            return new CustomMessageSerializerForTopics(
                List.of(SIMPLE_TOPIC, INPUT_TOPIC1, INPUT_TOPIC2),
                serializer
            );
        }

        @Bean
        CustomMessageDeserializerForTopics messageDeserializerForTopics(MessageDeserializer deserializer) {
            return new CustomMessageDeserializerForTopics(
                List.of(SIMPLE_TOPIC, INPUT_TOPIC1, INPUT_TOPIC2),
                deserializer
            );
        }

        @Bean
        MessageSerializer messageSerializer() {
            return new MessageSerializer() {

                @Override
                public byte[] serialize(Message message) throws Exception.FailedSerialize {
                    try {
                        return MessageDTO.getEncoder().encode(
                            new MessageDTO(
                                message.id(),
                                message.type(),
                                message.correlationId().orElse(null),
                                message.replyTo().orElse(null),
                                message.payload()
                            )
                        ).array();
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

        @Bean
        CustomTopologyForTopic customTopologyForTopic(Serde<Message> valueSerde) {
            return new CustomTopologyForTopic(
                TOPIC_WITH_JOIN,
                (String topic, Optional<Set<String>> messageTypes, Consumer<Message> consumer) -> {

                    StreamsBuilder builder = new StreamsBuilder();

                    final KStream<String, Message> s = builder.stream(INPUT_TOPIC1, Consumed.with(Serdes.String(), valueSerde))
                        .selectKey((k, v) -> {
                            return v.payloadAs(TestMessageDTO.class).getPayload().toString();
                        });

                    final var t = builder.stream(INPUT_TOPIC2, Consumed.with(Serdes.String(), valueSerde))
                        .selectKey((k, v) -> v.payloadAs(TestMessageDTO.class).getPayload().toString())
                        .toTable(Materialized.with(Serdes.String(), valueSerde));

                    s.join(
                            t,
                            (v1, v2) -> new Message(
                                v1.id(),
                                v1.type(),
                                v1.sentAt(),
                                Optional.empty(),
                                Optional.empty(),
                                new JoinedMessage(v1.id(), v2.id())
                            ),
                            Joined.with(Serdes.String(), valueSerde, valueSerde)
                        )
                        .foreach((k, message) -> {

                            try {
                                consumer.accept(message);
                            } catch (Exception e) {

                                throw e;
                            }
                        });


                    return builder.build();
                });
        }
    }
}
