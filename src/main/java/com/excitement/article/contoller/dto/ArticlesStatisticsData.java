package com.excitement.article.contoller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ArticlesStatisticsData {
    private LocalDate date;
    private long count;
}
