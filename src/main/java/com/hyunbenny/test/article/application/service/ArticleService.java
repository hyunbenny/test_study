package com.hyunbenny.test.article.application.service;

import com.hyunbenny.test.article.adapter.in.api.dto.ArticleDto;
import com.hyunbenny.test.article.application.port.in.CreateArticleUseCase;
import com.hyunbenny.test.article.application.port.in.DeleteArticleUseCase;
import com.hyunbenny.test.article.application.port.in.GetArticleUseCase;
import com.hyunbenny.test.article.application.port.in.ModifyArticleUseCase;
import com.hyunbenny.test.article.application.port.out.CommandArticlePort;
import com.hyunbenny.test.article.application.port.out.LoadArticlePort;
import com.hyunbenny.test.article.application.port.out.LoadBoardPort;
import com.hyunbenny.test.article.domain.Article;
import com.hyunbenny.test.common.exception.AccessDeniedException;
import com.hyunbenny.test.common.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService implements GetArticleUseCase, CreateArticleUseCase, ModifyArticleUseCase, DeleteArticleUseCase {

    private final LoadArticlePort loadArticlePort;
    private final CommandArticlePort commandArticlePort;
    private final LoadBoardPort loadBoardPort;


    @Override
    @Transactional(readOnly = true)
    public Article getArticleById(Long articleId) {
        return loadArticlePort.findArticleById(articleId).orElseThrow(() -> new ResourceNotFoundException("id: " + articleId + " 게시물이 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> getArticlesByBoard(Long boardId) {
        return loadArticlePort.findArticlesByBoardId(boardId);
    }

    @Override
    public Article createArticle(ArticleDto.CreateArticleRequest request) {
        Assert.hasLength(request.subject(), "subject should not empty");
        Assert.hasLength(request.content(), "content should not empty");
        Assert.hasLength(request.username(), "username should not empty");

        var board = loadBoardPort.findBoardById(request.boardId())
                .orElseThrow();
        var article = Article.builder()
                .board(board)
                .subject(request.subject())
                .content(request.content())
                .username(request.username())
                .createdAt(LocalDateTime.now())
                .build();

        return commandArticlePort.createArticle(article);
    }

    @Override
    public Article modifyArticle(ArticleDto.UpdateArticleRequest request) {
        Article article = loadArticlePort.findArticleById(request.id())
                .orElseThrow();

        if (!article.getUsername().equals(request.username())) {
            throw new AccessDeniedException("");
        }

        article.update(request.subject(), request.content());
        return commandArticlePort.modifyArticle(article);
    }

    @Override
    public void deleteArticle(Long articleId) {
        commandArticlePort.deleteArticle(articleId);
    }

}
