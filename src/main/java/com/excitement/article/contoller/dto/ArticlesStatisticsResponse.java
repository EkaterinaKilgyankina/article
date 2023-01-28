package com.excitement.article.contoller.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticlesStatisticsResponse {
    private List<ArticlesStatisticsData> statistics;
}
