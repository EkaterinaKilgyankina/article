package com.excitement.article.service;

import com.excitement.article.contoller.dto.ArticleDto;
import com.excitement.article.contoller.dto.CreateArticleRequest;
import com.excitement.article.contoller.dto.FindArticlesResponse;
import com.excitement.article.exception.UserNotFoundException;
import com.excitement.article.mapper.ArticleMapper;
import com.excitement.article.repository.ArticleRepository;
import com.excitement.article.repository.ClientRepository;
import com.excitement.article.repository.model.Article;
import com.excitement.article.repository.model.Client;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {
    private final ArticleRepository repositoryService;
    private final ClientRepository clientRepository;
    private final ArticleMapper mapper;

    public ArticleDto create(CreateArticleRequest request, User user) {
        Client author = clientRepository.findByUserName(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException("client not found by userName - " + user.getUsername()));

        Article article = mapper.toModel(request, author.getId(), LocalDate.now());
        Article saved = repositoryService.save(article);

        return mapper.toDto(saved);
    }

    public FindArticlesResponse findAll(int page, int size) {
        Page<Article> result = repositoryService.findAll(PageRequest.of(page, size));
        List<ArticleDto> articles = result.getContent()
                .stream()
                .map(mapper::toDto)
                .toList();

        return new FindArticlesResponse()
                .setArticles(articles)
                .setTotalElements(result.getTotalElements())
                .setPages(result.getTotalPages());
    }
}
