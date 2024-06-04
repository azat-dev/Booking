package com.azat4dev.booking.listingsms.helpers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public interface PostgresTests {
    @Container
    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:15-alpine"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", () -> {
            System.out.println("JDBC START!!!!" + postgres.getJdbcUrl());
            return postgres.getJdbcUrl();
        });
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    default void beforeEachPostgres() {
        System.out.println("JDBC!!!!" + postgres.getJdbcUrl());
    }

    @AfterEach
    default void afterEachPostgres() {
    }
}