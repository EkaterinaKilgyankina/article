package com.excitement.article.repository.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Article {
    private @Id Long id;
    private Long authorId;
    private String title;
    private String content;
    private LocalDateTime publishedAt;
    private LocalDate createdOn;
}
