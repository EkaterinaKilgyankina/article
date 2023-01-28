
package com.excitement.article;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;

public class PostgresTestContainer {
    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    static {
        Startables.deepStart(Collections.singletonList(POSTGRES_CONTAINER)).join();
    }

    public static void properties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
    }
}
