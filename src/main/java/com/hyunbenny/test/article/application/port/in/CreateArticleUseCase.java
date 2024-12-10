package com.hyunbenny.test.article.application.port.in;

import com.hyunbenny.test.article.adapter.in.api.dto.ArticleDto;
import com.hyunbenny.test.article.domain.Article;

public interface CreateArticleUseCase {
    Article createArticle(ArticleDto.CreateArticleRequest request);
}
