package com.excitement.article.contoller;

import com.excitement.article.contoller.dto.ArticleDto;
import com.excitement.article.contoller.dto.CreateArticleRequest;
import com.excitement.article.contoller.dto.FindArticlesResponse;
import com.excitement.article.service.ArticleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

/*
- To create an article the user should provide a title, author, the content and date for publishing.
- All of the fields are mandatory and the title should not exceed 100 characters. The publishing date should bind to ISO 8601 format.
- Article results should be paginated.

 */
@RestController
@RequestMapping(path = "/api/v1/articles")
@AllArgsConstructor
public class ArticleController {
    private final ArticleService service;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("""
            hasAnyAuthority(T(com.excitement.article.repository.model.Role).USER) || hasAnyAuthority(T(com.excitement.article.repository.model.Role).ADMIN)
            """)
    public ArticleDto publishArticle(
            @Valid @RequestBody CreateArticleRequest request,
            @AuthenticationPrincipal User user) {
        return service.create(request, user);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("""
            hasAnyAuthority(T(com.excitement.article.repository.model.Role).USER) || hasAnyAuthority(T(com.excitement.article.repository.model.Role).ADMIN)
            """)
    public FindArticlesResponse listArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.findAll(page, size);
    }
}
