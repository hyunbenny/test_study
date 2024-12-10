package com.hyunbenny.test.article.application.port.in;

import com.hyunbenny.test.article.domain.Article;

import java.util.List;

public interface GetArticleUseCase {
    Article getArticleById(Long articleId);

    List<Article> getArticlesByBoard(Long boardId);
}
