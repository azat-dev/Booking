package com.azat4dev.booking.searchlistingsms.helpers;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

public class KafkaTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {

        final var kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
        Startables.deepStart(kafka).join();

        TestPropertyValues.of(
            "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers()
        ).applyTo(ctx.getEnvironment());

        ctx.getBeanFactory().registerSingleton("kafka", kafka);
    }
}