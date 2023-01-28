package com.excitement.article.contoller.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class FindArticlesResponse {
    private List<ArticleDto> articles;
    private long totalElements;
    private int pages;
}
