package com.azat4dev.booking.helpers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.ShellStrategy;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@Slf4j
class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.1"));

    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    static MinIOContainer minio = new MinIOContainer("minio/minio:latest")
        .waitingFor(new ShellStrategy().withCommand("cd /data && mc mb users-photo"));

    static {
        Startables.deepStart(postgres, kafka, minio).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {

        log.atInfo()
            .addArgument(kafka.getBootstrapServers())
            .log("Kafka bootstrap servers: {}");

        TestPropertyValues.of(
            "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers(),
            "spring.datasource.url=" + postgres.getJdbcUrl(),
            "spring.datasource.username=" + postgres.getUsername(),
            "spring.datasource.password=" + postgres.getPassword(),
            "app.objects-storage.bucket.users-photo.endpoint=" + "http://" + minio.getHost() + ":" + minio.getMappedPort(9000),
            "app.objects-storage.bucket.users-photo.access-key=" + minio.getUserName(),
            "app.objects-storage.bucket.users-photo.secret-key=" + minio.getPassword()
        ).applyTo(ctx.getEnvironment());
    }
}