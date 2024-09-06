package com.azat4dev.booking.shared.helpers;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

class KafkaTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    static {
        Startables.deepStart(kafka).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {

        TestPropertyValues.of(
            "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers()
            ).applyTo(ctx.getEnvironment());
    }
}