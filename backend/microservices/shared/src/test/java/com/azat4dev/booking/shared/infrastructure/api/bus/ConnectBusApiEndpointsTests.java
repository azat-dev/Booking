package com.azat4dev.booking.shared.infrastructure.api.bus;


import com.azat4dev.booking.shared.config.infrastracture.api.AutoConnectApiEndpointsToBus;
import com.azat4dev.booking.shared.config.infrastracture.bus.DefaultMessageBusConfig;
import com.azat4dev.booking.shared.config.infrastracture.serializers.DefaultTimeSerializerConfig;
import com.azat4dev.booking.shared.config.infrastracture.services.DefaultTimeProviderConfig;
import com.azat4dev.booking.shared.domain.events.EventIdGenerator;
import com.azat4dev.booking.shared.domain.events.RandomEventIdGenerator;
import com.azat4dev.booking.shared.generated.dto.MessageDTO;
import com.azat4dev.booking.shared.generated.dto.TestMessageDTO;
import com.azat4dev.booking.shared.helpers.KafkaTestContainerInitializer;
import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import com.azat4dev.booking.shared.infrastructure.bus.NewMessageListenerForChannel;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.NewDeserializerForChannels;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.NewSerializerForChannels;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageDeserializer;
import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageSerializer;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

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

@DirtiesContext
@EnableKafka
@AutoConnectApiEndpointsToBus
@ContextConfiguration(initializers = {KafkaTestContainerInitializer.class})
@Import({
    DefaultMessageBusConfig.class,
    DefaultTimeProviderConfig.class,
    DefaultTimeSerializerConfig.class,
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ConnectBusApiEndpointsTests {

    private static final String INPUT_TOPIC = "input_topic";
    private static final String REPLY_TOPIC = "reply_topic";

    @Qualifier("endpointCallback")
    @Autowired
    AtomicReference<Consumer<TestMessageDTO>> endpointCallback;

    @BeforeEach
    void setUp() {
        endpointCallback.set(null);
    }

    @Autowired
    MessageBus messageBus;

    private String anyDomainEventId() {
        return UUID.randomUUID().toString();
    }

    private TestMessageDTO anyTestInputMessage() {
        return new TestMessageDTO(anyDomainEventId(), "payload");
    }

    @Test
    void test_publish_givenMessagePublished_thenEndpointMustBeTriggered() {

        // Given
        final var inputMessage = anyTestInputMessage();

        final var receivedReplyStore = new AtomicReference<TestMessageDTO>();
        endpointCallback.set(receivedReplyStore::set);

        // When
        messageBus.publish(
            INPUT_TOPIC,
            Optional.<String>empty(),
            MessageBus.Data.with(
                "messageId",
                "TestMessage",
                inputMessage
            )
        );

        // Then
        waitForValue(receivedReplyStore, Duration.ofSeconds(1));

        final var receivedReply = receivedReplyStore.get();
        assertThat(receivedReply).isEqualTo(inputMessage);
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
        EventIdGenerator eventIdGenerator() {
            return new RandomEventIdGenerator();
        }

        @Bean("endpointCallback")
        AtomicReference<Consumer<TestMessageDTO>> endpointCallback() {
            return new AtomicReference<>();
        }

        @Bean
        NewMessageListenerForChannel replyTopicListener() {
            return new NewMessageListenerForChannel(
                REPLY_TOPIC,
                message -> {
                    endpointCallback().get().accept(message.payloadAs(TestMessageDTO.class));
                }
            );
        }

        @Bean
        BusApiEndpoint<TestMessageDTO> testEndpoint() {
            return new BusApiEndpoint<>() {
                @Override
                public void handle(Request<TestMessageDTO> request, Reply reply) throws Exception {
                    reply.publish(request.message());
                }

                @Override
                public String getInputAddress() {
                    return INPUT_TOPIC;
                }

                @Override
                public boolean hasDynamicReplyAddress() {
                    return false;
                }

                @Override
                public Optional<String> getStaticReplyAddress() {
                    return Optional.of(REPLY_TOPIC);
                }

                @Override
                public MessageInfo[] getInputMessageInfo() {
                    return new MessageInfo[]{
                        new MessageInfo(
                            "TestMessage",
                            TestMessageDTO.class
                        )
                    };
                }

                @Override
                public MessageInfo[] getResponseMessagesInfo() {
                    return new MessageInfo[0];
                }

                @Override
                public boolean isMessageTypeAllowed(String messageType) {
                    return messageType.equals("TestMessage");
                }
            };
        }

        @Bean
        KafkaAdmin.NewTopics newTopicList() {
            return new KafkaAdmin.NewTopics(
                new NewTopic(INPUT_TOPIC, 1, (short) 1),
                new NewTopic(REPLY_TOPIC, 1, (short) 1)
            );
        }

        @Bean
        NewSerializerForChannels messageSerializerForInputTopic(MessageSerializer serializer) {
            return new NewSerializerForChannels(
                List.of(INPUT_TOPIC, REPLY_TOPIC),
                serializer
            );
        }

        @Bean
        NewDeserializerForChannels messageDeserializerForTopics(MessageDeserializer deserializer) {
            return new NewDeserializerForChannels(
                List.of(INPUT_TOPIC, REPLY_TOPIC),
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
