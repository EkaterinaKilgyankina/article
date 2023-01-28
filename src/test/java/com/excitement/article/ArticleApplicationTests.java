package com.excitement.article;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ArticleApplicationTests {

    @DynamicPropertySource
    public static void properties(final DynamicPropertyRegistry registry) {
        PostgresTestContainer.properties(registry);
    }

    @Test
    void contextLoads() {
    }
}
