package com.azat4dev.booking.users.config.users_commands.infrastructure.bus;

import com.azat4dev.booking.users.commands.application.commands.email.verification.CompleteEmailVerification;
import com.azat4dev.booking.users.commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.commands.domain.core.events.*;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    public Map<String, Object> producerConfigs() {

        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties(null));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        System.out.println("Using Kafka properties: " + props);
        return props;
    }

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties(null));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<String, String>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    KafkaAdmin.NewTopics newTopicList() {

        final List<Class<?>> events = List.of(
            UserSignedUp.class,

            SendVerificationEmail.class,
            VerificationEmailSent.class,
            FailedToSendVerificationEmail.class,
            UserVerifiedEmail.class,
            CompleteEmailVerification.class,

            SentEmailForPasswordReset.class,
            UserDidResetPassword.class,
            FailedToCompleteResetPassword.class,

            UpdatedUserPhoto.class,
            FailedUpdateUserPhoto.class
        );

        final var topics = events.stream().map(
            clazz -> new NewTopic(clazz.getSimpleName(), 1, (short) 1)
        ).toList().toArray(new NewTopic[0]);

        return new KafkaAdmin.NewTopics(topics);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Function<String, ConcurrentMessageListenerContainer<String, String>> containerFactory() {
        return (String topic) -> new ConcurrentMessageListenerContainer<>(
            consumerFactory(),
            new ContainerProperties(topic)
        );
    }
}
