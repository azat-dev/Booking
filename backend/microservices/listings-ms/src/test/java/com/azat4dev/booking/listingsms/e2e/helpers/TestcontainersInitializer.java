package com.azat4dev.booking.listingsms.e2e.helpers;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.ShellStrategy;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.1"));

    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    static MinIOContainer minio = new MinIOContainer("minio/minio:latest")
        .waitingFor(new ShellStrategy().withCommand("cd /data && mc mb listings-photo"));

    static {
        Startables.deepStart(postgres, kafka, minio).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {

        TestPropertyValues.of(
            "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers(),
            "spring.datasource.url=" + postgres.getJdbcUrl(),
            "spring.datasource.username=" + postgres.getUsername(),
            "spring.datasource.password=" + postgres.getPassword(),
            "app.objects-storage.bucket.listings-photo.endpoint=" + "http://" + minio.getHost() + ":" + minio.getMappedPort(9000),
            "app.objects-storage.bucket.listings-photo.access-key=" + minio.getUserName(),
            "app.objects-storage.bucket.listings-photo.secret-key=" + minio.getPassword()
        ).applyTo(ctx.getEnvironment());
    }
}