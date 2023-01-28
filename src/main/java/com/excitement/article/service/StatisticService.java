package com.excitement.article.service;

import com.excitement.article.contoller.dto.ArticlesStatisticsData;
import com.excitement.article.contoller.dto.ArticlesStatisticsResponse;
import com.excitement.article.repository.ArticleRepositoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StatisticService {
    private final ArticleRepositoryService articleRepositoryService;

    public ArticlesStatisticsResponse articlesStat() {
        List<ArticlesStatisticsData> data = articleRepositoryService.findStat();
        return new ArticlesStatisticsResponse()
                .setStatistics(data);
    }
}
