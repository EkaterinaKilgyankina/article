package com.excitement.article.repository;

import com.excitement.article.PostgresTestContainer;
import com.excitement.article.contoller.dto.ArticlesStatisticsData;
import com.excitement.article.repository.model.Article;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDateTime;
import java.util.List;

@ActiveProfiles("test")
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleRepositoryTest {
    @Autowired
    ArticleRepository articleRepository;
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
    void save() {
        jdbcTemplate.execute("""
                insert into client (id, user_name, password, user_role)
                values (11, 'name', 'pwd', 'USER')
                """);
        Article article = new Article()
                .setTitle("tt")
                .setContent("wss")
                .setAuthorId(11L)
                .setPublishedAt(LocalDateTime.now());

        Article result = articleRepository.save(article);

        Assertions.assertThat(result)
                .isNotNull()
                .matches(e -> e.getId() != null);
    }

    @Test
    void findAll() {
        jdbcTemplate.execute("""
                insert into client (id, user_name, password, user_role)
                values (12, 'name', 'pwd', 'USER');
                insert into article (id, author_id, title, content, published_at, created_on)
                values (1, 12, 'title1', 'content1', now(), now());
                insert into article (id, author_id, title, content, published_at, created_on)
                values (2, 12, 'title2', 'content2', now(), now());
                """);

        Page<Article> result = articleRepository.findAll(PageRequest.of(0, 1));

        Assertions.assertThat(result.getTotalElements())
                .isEqualTo(2);
        Assertions.assertThat(result.getTotalPages())
                .isEqualTo(2);
        Assertions.assertThat(result.getContent())
                .hasSize(1)
                .element(0)
                .matches(e -> e.getId() == 1L);
    }

    @Test
    void stat() {
        jdbcTemplate.execute("""
                insert into client (id, user_name, password, user_role)
                values (12, 'name', 'pwd', 'USER');
                insert into article (id, author_id, title, content, published_at, created_on)
                values (1, 12, 'title1', 'content1', now(), '2020-10-10');
                insert into article (id, author_id, title, content, published_at, created_on)
                values (2, 12, 'title2', 'content2', now(), '2020-10-11');
                insert into article (id, author_id, title, content, published_at, created_on)
                values (3, 12, 'title3', 'content3', now(), '2020-10-12');
                insert into article (id, author_id, title, content, published_at, created_on)
                values (4, 12, 'title4', 'content4', now(), '2020-10-13');
                insert into article (id, author_id, title, content, published_at, created_on)
                values (5, 12, 'title5', 'content5', now(), '2020-10-14');
                insert into article (id, author_id, title, content, published_at, created_on)
                values (6, 12, 'title6', 'content6', now(), '2020-10-15');
                insert into article (id, author_id, title, content, published_at, created_on)
                values (7, 12, 'title7', 'content7', now(), '2020-10-16');
                insert into article (id, author_id, title, content, published_at, created_on)
                values (8, 12, 'title8', 'content8', now(), '2020-10-17');
                insert into article (id, author_id, title, content, published_at, created_on)
                values (9, 12, 'title9', 'content9', now(), '2020-10-17');
                """);

        List<ArticlesStatisticsData> result = articleRepository.findStat();

        Assertions.assertThat(result)
                .hasSize(7)
                .matches(e -> e.get(0).getCount() == 2L)
                .filteredOn(e -> e.getCount() == 1)
                .hasSize(6);
    }
}