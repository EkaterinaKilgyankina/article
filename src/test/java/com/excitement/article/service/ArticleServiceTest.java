package com.excitement.article.service;

import com.excitement.article.contoller.dto.ArticleDto;
import com.excitement.article.contoller.dto.CreateArticleRequest;
import com.excitement.article.contoller.dto.FindArticlesResponse;
import com.excitement.article.exception.UserNotFoundException;
import com.excitement.article.mapper.ArticleMapper;
import com.excitement.article.repository.ArticleRepositoryService;
import com.excitement.article.repository.ClientRepository;
import com.excitement.article.repository.model.Article;
import com.excitement.article.repository.model.Client;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @InjectMocks
    ArticleService service;
    @Mock
    ArticleRepositoryService repositoryService;
    @Mock
    ClientRepository clientRepository;
    @Mock
    ArticleMapper mapper;

    @Test
    void create_authorNotFound() {
        CreateArticleRequest request = new CreateArticleRequest()
                .setContent("content");
        Mockito.when(clientRepository.findByUserName("userName"))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.create(request, new User("userName", "pwd", List.of())))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("client not found by userName - userName");
    }

    @Test
    void create() {
        CreateArticleRequest request = new CreateArticleRequest()
                .setContent("content");
        Article article = new Article()
                .setTitle("title");
        ArticleDto articleDto = new ArticleDto()
                .setContent("cc");
        Mockito.when(clientRepository.findByUserName("userName"))
                .thenReturn(Optional.of(
                        new Client()
                                .setId(123L)
                ));
        Mockito.when(mapper.toModel(request, 123L, LocalDate.now()))
                .thenReturn(article);
        Mockito.when(repositoryService.save(article)).thenAnswer(e -> e.getArgument(0));
        Mockito.when(mapper.toDto(article)).thenReturn(articleDto);

        ArticleDto result = service.create(request, new User("userName", "pwd", List.of()));

        Assertions.assertThat(result)
                .isEqualTo(articleDto);
    }

    @Test
    void findAll() {
        Article article = new Article()
                .setTitle("title1");
        ArticleDto dto = new ArticleDto()
                .setTitle("title2");
        Mockito.when(repositoryService.findAll(PageRequest.of(1, 11)))
                .thenReturn(
                        new PageImpl<>(List.of(article), PageRequest.of(1, 11), 10)
                );
        Mockito.when(mapper.toDto(article)).thenReturn(dto);

        FindArticlesResponse result = service.findAll(1, 11);

        Assertions.assertThat(result)
                .matches(e -> e.getTotalElements() == 12L)
                .matches(e -> e.getPages() == 2)
                .extracting(FindArticlesResponse::getArticles)
                .matches(e -> e.size() == 1)
                .extracting(e -> e.get(0))
                .isEqualTo(dto);
    }
}
