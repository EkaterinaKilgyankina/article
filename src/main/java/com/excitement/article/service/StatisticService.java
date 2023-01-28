package com.excitement.article.service;

import com.excitement.article.contoller.dto.ArticlesStatisticsData;
import com.excitement.article.contoller.dto.ArticlesStatisticsResponse;
import com.excitement.article.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StatisticService {
    private final ArticleRepository articleRepositoryService;

    public ArticlesStatisticsResponse articlesStat() {
        List<ArticlesStatisticsData> data = articleRepositoryService.findStat();
        return new ArticlesStatisticsResponse()
                .setStatistics(data);
    }
}
