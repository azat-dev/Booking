package com.azat4dev.booking.users.config.users_commands.infrastructure.bus;

import com.azat4dev.booking.users.commands.application.commands.email.verification.CompleteEmailVerification;
import com.azat4dev.booking.users.commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.commands.domain.core.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    public Map<String, Object> producerConfigs() {

        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties(null));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
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
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        final var template = new KafkaTemplate<>(producerFactory);
        template.setObservationEnabled(true);
        return template;
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

//    @Bean
//    ConcurrentKafkaListenerContainerFactory<Integer, String>
//    kafkaListenerContainerFactory(ConsumerFactory<Integer, String> consumerFactory) {
//        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
//            new ConcurrentKafkaListenerContainerFactory<>();
//
//        factory.getContainerProperties().setObservationEnabled(true);
//        factory.setConsumerFactory(consumerFactory);
//        return factory;
//    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> cf) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.getContainerProperties().setObservationEnabled(true);

        return factory;
    }


//    @Bean
//    public Function<String, ConcurrentMessageListenerContainer<String, String>> containerFactory(ConsumerFactory<String, String> consumerFactory) {
//        return (String topic) -> {
//            final var  props = new ContainerProperties(topic);
//            props.setObservationEnabled(true);
//
//            return new ConcurrentMessageListenerContainer<>(
//                consumerFactory,
//                props
//            );
//        };
//    }
}
