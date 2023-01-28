package com.excitement.article.repository;

import com.excitement.article.repository.model.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Long>, CrudRepository<Article, Long> {

}
