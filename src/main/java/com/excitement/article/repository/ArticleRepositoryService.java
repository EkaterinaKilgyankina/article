package com.excitement.article.repository;

import com.excitement.article.contoller.dto.ArticlesStatisticsData;
import com.excitement.article.repository.model.Article;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ArticleRepositoryService {
    private final ArticleRepository repository;
    private final JdbcTemplate jdbcTemplate;

    public Article save(Article article) {
        return repository.save(article);
    }

    public Page<Article> findAll(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }

    public List<ArticlesStatisticsData> findStat() {
        return jdbcTemplate.query("""
                                select created_on, count(*) from article
                                group by created_on order by created_on desc limit 7
                """, (rs, rowNum) -> {
            return new ArticlesStatisticsData()
                    .setDate(rs.getObject("created_on", LocalDate.class))
                    .setCount(rs.getLong("count"));
        });
    }
}
