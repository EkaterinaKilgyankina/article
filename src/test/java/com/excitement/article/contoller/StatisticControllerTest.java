package com.excitement.article.contoller;

import com.excitement.article.config.SecurityConfig;
import com.excitement.article.config.security.UserDetailsCustomService;
import com.excitement.article.contoller.dto.ArticlesStatisticsData;
import com.excitement.article.contoller.dto.ArticlesStatisticsResponse;
import com.excitement.article.service.StatisticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@WebMvcTest(controllers = StatisticController.class)
@Import(SecurityConfig.class)
class StatisticControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    StatisticService service;
    //for security
    @MockBean
    UserDetailsCustomService userDetailsCustomService;
    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void articlesStat() throws Exception {
        Mockito.when(service.articlesStat()).thenReturn(
                new ArticlesStatisticsResponse()
                        .setStatistics(List.of(
                                new ArticlesStatisticsData()
                                        .setDate(LocalDate.of(2020, 10, 12))
                                        .setCount(8)
                        ))
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/statistics/articles")
                        .accept(APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                           "statistics":[
                              {
                                 "date":"2020-10-12",
                                 "count":8
                              }
                           ]
                        }
                        """));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void articlesStat_forbidden() throws Exception {
        Mockito.when(service.articlesStat()).thenReturn(
                new ArticlesStatisticsResponse()
                        .setStatistics(List.of(
                                new ArticlesStatisticsData()
                                        .setDate(LocalDate.of(2020, 10, 12))
                                        .setCount(8)
                        ))
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/statistics/articles")
                        .accept(APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                           "code":"FORBIDDEN",
                           "message":"Forbidden"
                        }
                        """));

    }
}
