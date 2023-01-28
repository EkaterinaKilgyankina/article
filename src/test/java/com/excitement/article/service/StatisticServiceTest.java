package com.excitement.article.service;

import com.excitement.article.contoller.dto.ArticlesStatisticsData;
import com.excitement.article.contoller.dto.ArticlesStatisticsResponse;
import com.excitement.article.repository.ArticleRepositoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest {
    @InjectMocks
    StatisticService service;
    @Mock
    ArticleRepositoryService articleRepositoryService;

    @Test
    void articlesStat() {
        ArticlesStatisticsData data = new ArticlesStatisticsData()
                .setDate(LocalDate.now())
                .setCount(12);
        Mockito.when(articleRepositoryService.findStat())
                .thenReturn(List.of(data));

        ArticlesStatisticsResponse result = service.articlesStat();

        Assertions.assertThat(result)
                .isNotNull()
                .extracting(ArticlesStatisticsResponse::getStatistics)
                .matches(e -> e.size() == 1)
                .extracting(e -> e.get(0))
                .isEqualTo(data);
    }
}
