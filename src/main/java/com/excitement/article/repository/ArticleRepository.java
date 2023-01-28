package com.excitement.article.repository;

import com.excitement.article.contoller.dto.ArticlesStatisticsData;
import com.excitement.article.repository.model.Article;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Long>, CrudRepository<Article, Long> {

    @Query("""
            select created_on, count(*) from article
            group by created_on order by created_on desc limit 7
            """)
    List<ArticlesStatisticsData> findStat();

}
