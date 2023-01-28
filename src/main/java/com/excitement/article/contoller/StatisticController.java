package com.excitement.article.contoller;


import com.excitement.article.contoller.dto.ArticlesStatisticsResponse;
import com.excitement.article.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/statistics")
@AllArgsConstructor
public class StatisticController {
    private final StatisticService service;

    @GetMapping(
            path = "/articles",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("""
            hasAnyAuthority(T(com.excitement.article.repository.model.Role).ADMIN)
            """)
    public ArticlesStatisticsResponse articlesStat() {
        return service.articlesStat();
    }
}
