package com.hyunbenny.test.article.application.port.out;

import com.hyunbenny.test.article.domain.Article;

public interface CommandArticlePort {
    Article createArticle(Article article);

    Article modifyArticle(Article article);

    void deleteArticle(Long articleId);
}
