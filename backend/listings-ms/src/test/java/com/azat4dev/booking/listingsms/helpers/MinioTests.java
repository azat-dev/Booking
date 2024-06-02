package com.azat4dev.booking.listingsms.helpers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.wait.strategy.ShellStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public interface MinioTests {

    @Container
    MinIOContainer minio = new MinIOContainer("minio/minio:latest")
        .waitingFor(new ShellStrategy().withCommand("cd /data && mc mb listings-photo"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("app.objects_storage.bucket.listings-photo.endpoint", () -> "http://" + minio.getHost() + ":" + minio.getMappedPort(9000));
        registry.add("app.objects_storage.bucket.listings-photo.access-key", minio::getUserName);
        registry.add("app.objects_storage.bucket.listings-photo.secret-key", minio::getPassword);
    }
}