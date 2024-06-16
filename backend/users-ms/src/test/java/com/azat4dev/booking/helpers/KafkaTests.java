package com.azat4dev.booking.helpers;

import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class KafkaTests implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    @Container
    private static KafkaContainer kafka;

    @Override
    public void beforeAll(ExtensionContext context) {

    }

    @Override
    public void afterAll(ExtensionContext context) {
//        kafka.close();
//        kafka.stop();
        // do nothing, Testcontainers handles container shutdown
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
        );

        kafka.start();

        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        kafka.close();
        kafka.stop();
    }
}

