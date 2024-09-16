package com.azat4dev.booking.shared.infrastructure.api.bus;


import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultMessageBusConfig;
import com.azat4dev.booking.shared.config.infrastracture.serializers.DefaultTimeSerializerConfig;
import com.azat4dev.booking.shared.config.infrastracture.services.DefaultTimeProviderConfig;
import com.azat4dev.booking.shared.generated.dto.JoinedMessageDTO;
import com.azat4dev.booking.shared.generated.dto.MessageDTO;
import com.azat4dev.booking.shared.generated.dto.TestMessageDTO;
import com.azat4dev.booking.shared.helpers.EnableTestcontainers;
import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.NewTopicListener;
import com.azat4dev.booking.shared.infrastructure.bus.kafka.GetSerdeForTopic;
import com.azat4dev.booking.shared.infrastructure.bus.kafka.StreamFactoryForTopic;
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
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
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
    DefaultMessageBusConfig.class,
    DefaultTimeProviderConfig.class,
    DefaultTimeSerializerConfig.class
})
@EnableTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class KafkaMessageBusTests {

    private static final String SIMPLE_TOPIC = "simple_topic";
    private static final String TOPIC_WITH_JOIN = "topic_with_join";
    private static final String INPUT_TOPIC1 = "input1";
    private static final String INPUT_TOPIC2 = "input2";

    @Qualifier("simpleTopicListenerCallback")
    @Autowired
    AtomicReference<Consumer<Message>> simpleTopicListener;

    @Qualifier("topicWithJoinListenerCallback")
    @Autowired
    AtomicReference<Consumer<Message>> topicWithJoinListener;

    @BeforeEach
    void setUp() {
        simpleTopicListener.set(null);
        topicWithJoinListener.set(null);
    }

    @Autowired
    MessageBus messageBus;

    private String anyMessageId() {
        return UUID.randomUUID().toString();
    }

    private Message anyTestMessage() {
        return new Message(
            anyMessageId(),
            "TestMessage",
            LocalDateTime.now(),
            Optional.empty(),
            Optional.empty(),
            new TestMessageDTO("id", "payload")
        );
    }

    @Test
    void test_publish_givenValidMessage_thenReceive() {

        // Given
        final var topic = SIMPLE_TOPIC;
        final var message = anyTestMessage();

        final var receivedMessageStore = new AtomicReference<Message>();
        simpleTopicListener.set(receivedMessageStore::set);

        // When
        messageBus.publish(
            topic,
            Optional.of(message.id()),
            MessageBus.Data.with(
                message.id(),
                message.type(),
                message.payload()
            )
        );

        // Then
        waitForValue(receivedMessageStore, Duration.ofSeconds(1));

        final var receivedMessage = receivedMessageStore.get();
        assertThat(receivedMessage.payload()).isEqualTo(message.payload());
    }

    @Test
    void test_publish_givenTwoInputTopics_thenJoin() {

        // Given
        final var message1 = anyTestMessage();
        final var message2 = anyTestMessage();

        final var receivedMessageStore = new AtomicReference<Message>();
        topicWithJoinListener.set(receivedMessageStore::set);

        // When
        messageBus.publish(
            INPUT_TOPIC1,
            Optional.empty(),
            MessageBus.Data.with(
                message1.id(),
                message1.type(),
                message1.payload()
            )
        );

        messageBus.publish(
            INPUT_TOPIC2,
            Optional.empty(),
            MessageBus.Data.with(
                message2.id(),
                message2.type(),
                message2.payload()
            )
        );

        // Then
        waitForValue(receivedMessageStore, Duration.ofSeconds(1));

        final var expected = new JoinedMessageDTO(message1.id(), message2.id());

        final var receivedMessage = receivedMessageStore.get();
        assertThat(receivedMessage.payload()).isEqualTo(expected);
    }

    @SpringBootApplication
    public static class TestApp {

        public static void main(String[] args) {
            SpringApplication.run(TestApp.class, args);
        }
    }

    @TestConfiguration
    public static class TestConfig {

        @Autowired
        KafkaAdmin admin;

        @Bean("simpleTopicListenerCallback")
        AtomicReference<Consumer<Message>> simpleTopicListenerCallback() {
            return new AtomicReference<>();
        }

        @Bean("topicWithJoinListenerCallback")
        AtomicReference<Consumer<Message>> topicWithJoinListenerCallback() {
            return new AtomicReference<>();
        }

        @Bean
        KafkaAdmin.NewTopics newTopicList() {
            return new KafkaAdmin.NewTopics(
                new NewTopic(SIMPLE_TOPIC, 1, (short) 1),
                new NewTopic(INPUT_TOPIC1, 1, (short) 1),
                new NewTopic(INPUT_TOPIC2, 1, (short) 1)
            );
        }

        @Bean
        NewTopicListener simpleTopicListener(
            @Qualifier("simpleTopicListenerCallback")
            AtomicReference<Consumer<Message>> simpleTopicListenerCallback
        ) {
            return new NewTopicListener(
                SIMPLE_TOPIC,
                message -> {
                    simpleTopicListenerCallback.get().accept(message);
                }
            );
        }

        @Bean
        NewTopicListener topicWithJoinListener(
            @Qualifier("topicWithJoinListenerCallback")
            AtomicReference<Consumer<Message>> topicWithJoinListenerCallback
        ) {
            return new NewTopicListener(
                TOPIC_WITH_JOIN,
                message -> {
                    topicWithJoinListenerCallback.get().accept(message);
                }
            );
        }

        @Bean
        Serde<JoinedMessageDTO> joinedMessageSerde(ObjectMapper objectMapper) {
            return new Serde<>() {
                @Override
                public Serializer<JoinedMessageDTO> serializer() {
                    return (s, joinedMessage) -> {
                        try {
                            return objectMapper.writeValueAsBytes(joinedMessage);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    };
                }

                @Override
                public Deserializer<JoinedMessageDTO> deserializer() {
                    return (s, bytes) -> {
                        try {
                            return objectMapper.readValue(bytes, JoinedMessageDTO.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
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


        @Bean
        StreamFactoryForTopic streamFactoryForJoinTopic(GetSerdeForTopic getSerdeForTopic) {
            return new StreamFactoryForTopic(
                TOPIC_WITH_JOIN,
                builder -> {

                    final KStream<String, Message> s = builder.stream(INPUT_TOPIC1, Consumed.with(Serdes.String(), getSerdeForTopic.run(INPUT_TOPIC1)))
                        .selectKey((k, v) -> {
                            return v.payloadAs(TestMessageDTO.class).getPayload().toString();
                        });

                    final var t = builder.stream(INPUT_TOPIC2, Consumed.with(Serdes.String(), getSerdeForTopic.run(INPUT_TOPIC2)))
                        .selectKey((k, v) -> v.payloadAs(TestMessageDTO.class).getPayload().toString())
                        .toTable(Materialized.with(Serdes.String(), getSerdeForTopic.run(INPUT_TOPIC2)));

                    return s.join(
                        t,
                        (v1, v2) -> new Message(
                            v1.id(),
                            v1.type(),
                            v1.sentAt(),
                            Optional.empty(),
                            Optional.empty(),
                            new JoinedMessageDTO(v1.id(), v2.id())
                        ),
                        Joined.with(Serdes.String(), getSerdeForTopic.run(INPUT_TOPIC1), getSerdeForTopic.run(INPUT_TOPIC2))
                    );
                }
            );
        }
    }
}
