package com.hyunbenny.test.article.application.service;

import com.hyunbenny.test.article.domain.Article;
import com.hyunbenny.test.article.domain.Board;

import java.time.LocalDateTime;

public class ArticleFixtures {
    public static Article article(Long id) {
        var board = Board.builder()
            .id(5L)
            .name("board")
            .build();

        return Article.builder()
                .id(id)
                .board(board)
                .subject("subject" + id)
                .content("content" + id)
                .username("user" + id)
                .createdAt(LocalDateTime.parse("2024-12-10T15:33:12").plusDays(id))
                .build();
    }

    public static Article article() {
        Long id = 1L;
       return article(id);
    }
}
