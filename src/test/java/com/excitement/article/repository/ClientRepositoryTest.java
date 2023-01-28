package com.excitement.article.repository;

import com.excitement.article.PostgresTestContainer;
import com.excitement.article.repository.model.Client;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Optional;

@ActiveProfiles("test")
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryTest {
    @Autowired
    ClientRepository repository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    public static void properties(final DynamicPropertyRegistry registry) {
        PostgresTestContainer.properties(registry);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("""
                delete from article;
                delete from client;
                """);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("""
                delete from article;
                delete from client;
                """);
    }

    @Test
    void findByUserName() {
        jdbcTemplate.execute("""
                insert into client (id, user_name, password, user_role)
                values (11, 'name', 'pwd', 'USER')
                """);

        Optional<Client> result = repository.findByUserName("name");

        Assertions.assertThat(result)
                .isPresent()
                .get()
                .matches(e -> e.getId() == 11L);
    }
}
