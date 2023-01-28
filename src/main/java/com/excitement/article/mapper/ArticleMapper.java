package com.excitement.article.mapper;

import com.excitement.article.contoller.dto.ArticleDto;
import com.excitement.article.contoller.dto.CreateArticleRequest;
import com.excitement.article.repository.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(target = "authorId", source = "authorId")
    @Mapping(target = "createdOn", source = "createdOn")
    @Mapping(target = "id", ignore = true)
    Article toModel(CreateArticleRequest articleDto, Long authorId, LocalDate createdOn);

    ArticleDto toDto(Article article);
}
