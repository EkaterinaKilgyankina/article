package com.excitement.article.contoller;

import com.excitement.article.config.SecurityConfig;
import com.excitement.article.config.security.UserDetailsCustomService;
import com.excitement.article.contoller.dto.ArticleDto;
import com.excitement.article.contoller.dto.CreateArticleRequest;
import com.excitement.article.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@WebMvcTest(controllers = ArticleController.class)
@Import(SecurityConfig.class)
class ArticleControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ArticleService service;
    //for security
    @MockBean
    UserDetailsCustomService userDetailsCustomService;
    @MockBean
    PasswordEncoder passwordEncoder;


    private static Stream<Arguments> values() {
        return Stream.of(
                Arguments.of("content", null, "content must not be blank"),
                Arguments.of("title", null, "title must not be blank"),
                Arguments.of("title", RandomStringUtils.random(101), "title size must be between 0 and 100"),
                Arguments.of("publishedAt", null, "publishedAt must not be null")
        );
    }

    @ParameterizedTest
    @MethodSource("values")
    @WithMockUser(authorities = "USER")
    void validations(String fieldName, String value, String errorMessage) throws Exception {
        CreateArticleRequest request = createRequest();
        ReflectionTestUtils.setField(request, fieldName, value);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/articles")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                           "code":"VALIDATION_ERROR",
                           "message":"%s"
                        }
                        """.formatted(errorMessage)));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void create() throws Exception {
        CreateArticleRequest request = createRequest();
        Mockito.when(service.create(Mockito.eq(request), Mockito.any())).thenReturn(
                new ArticleDto()
                        .setContent("qqq")
                        .setTitle("ggg")
                        .setAuthor("www")
                        .setPublishedAt(request.getPublishedAt())
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/articles")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                           "title":"ggg",
                           "author":"www",
                           "content":"qqq",
                           "publishedAt":"2023-10-10 10:00:00"
                        }
                        """));
    }

    @Test
    void create_forbidden() throws Exception {
        CreateArticleRequest request = createRequest();
        Mockito.when(service.create(Mockito.eq(request), Mockito.any())).thenReturn(
                new ArticleDto()
                        .setContent("qqq")
                        .setTitle("ggg")
                        .setAuthor("www")
                        .setPublishedAt(request.getPublishedAt())
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/articles")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    private CreateArticleRequest createRequest() {
        return new CreateArticleRequest()
                .setContent("content")
                .setTitle("title")
                .setPublishedAt(LocalDateTime.of(2023, 10, 10, 10, 0, 0));
    }
}