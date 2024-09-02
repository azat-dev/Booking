package com.azat4dev.booking.shared.infrastructure.api.bus;


import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultMessageBusConfig;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.bus.utils.OneToOneRelationsOfDtoClassesAndMessageTypes;
import com.azat4dev.booking.shared.config.infrastracture.serializers.DefaultTimeSerializerConfig;
import com.azat4dev.booking.shared.config.infrastracture.services.DefaultTimeProviderConfig;
import com.azat4dev.booking.shared.helpers.EnableTestcontainers;
import com.azat4dev.booking.shared.infrastructure.bus.CustomTopologyFactoriesForTopics;
import com.azat4dev.booking.shared.infrastructure.bus.CustomTopologyForTopic;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
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

import java.time.Duration;
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

    private static final String SIMPLE_TOPIC = "test";
    private static final String TOPIC_WITH_JOIN = "test_join";
    private static final String INPUT_TOPIC1 = "input1";
    private static final String INPUT_TOPIC2 = "input2";

    @Autowired
    MessageBus<String> messageBus;

    @Test
    void test_publish_givenValidMessage_thenReceive() {

        // Given
        final var topic = SIMPLE_TOPIC;
        final var messageId = UUID.randomUUID().toString();
        final var messageType = "TestMessage";
        final var message = new TestMessage("id", "type", "payload");

        // When
        messageBus.publish(
            topic,
            Optional.empty(),
            messageId,
            messageType,
            message
        );

        // Then
        final var receivedMessage = waitForResult((Consumer<MessageBus.ReceivedMessage> completed) -> {
            messageBus.listen(topic, msg -> {
                completed.accept(msg);
            });
        });

        assertThat(receivedMessage.payload()).isEqualTo(message);
    }

    @Test
    void test_publish_givenTwoInputTopics_thenJoin() {

        // Given
        final var topic = "test";
        final var messageId1 = UUID.randomUUID().toString();
        final var messageId2 = UUID.randomUUID().toString();
        final var messageType = "TestMessage";

        // When
        messageBus.publish(
            topic,
            Optional.empty(),
            messageId1,
            messageType,
            new TestMessage(messageId1, "type", "payload")
        );

        messageBus.publish(
            topic,
            Optional.empty(),
            messageId2,
            messageType,
            new TestMessage(messageId2, "type", "payload")
        );

        // Then
        final var expected = new JoinedMessage(messageId1, messageId2);

        final var receivedMessage = waitForResult((Consumer<MessageBus.ReceivedMessage> completed) -> {
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
                new OneToOneRelationsOfDtoClassesAndMessageTypes.Item("TestMessage", TestMessage.class)
            );
        }

        @Bean
        CustomTopologyForTopic customTopologyForTopic(Serde<MessageBus.ReceivedMessage> valueSerde) {
            return new CustomTopologyForTopic(
                TOPIC_WITH_JOIN,
                (String topic, Optional<Set<String>> messageTypes, Consumer<MessageBus.ReceivedMessage> consumer) -> {

                    StreamsBuilder builder = new StreamsBuilder();

                    final var s = builder.stream(INPUT_TOPIC1, Consumed.with(Serdes.String(), valueSerde))
                        .selectKey((k, v) -> v.payload());

                    final var t = builder.stream(INPUT_TOPIC2, Consumed.with(Serdes.String(), valueSerde))
                        .selectKey((k, v) -> v.payload())
                        .toTable();

                    s.join(t, (left, right) -> new JoinedMessage(left.messageId(), right.messageId()))
                        .to(TOPIC_WITH_JOIN);

                    return builder.build();
                });
        }
    }

    public static record TestMessage(String id, String type, String payload) {
    }

    public static record JoinedMessage(String messageId1, String messageId2) {
    }
}
