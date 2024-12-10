package com.hyunbenny.test;

import com.hyunbenny.test.article.domain.Article;
import com.hyunbenny.test.article.domain.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.*;

public class AssertJTest {
    private Board board;

    @BeforeEach
    void setup() {
        board = Board.builder().id(10L).name("board").build();
    }

    @Test
    @DisplayName("assertj assertion - 게시글 수정")
    void updateArticle() {
        // given
        var article = Article.builder()
                .id(1L)
                .board(board)
                .username("user")
                .subject("subject")
                .content("content")
                .createdAt(LocalDateTime.now())
                .build();

        // when
        String updateSubject = "updated subject";
        String updateContent = "updated content";
        article.update(updateSubject, updateContent);

        // then
//        assertThat(article.getSubject()).isEqualTo(updateSubject);
//        assertThat(article.getContent()).isEqualTo(updateContent);

        // 위와 같이 하나하나 검증하는 방법도 있지만 assertJ의 assertThat을 통해서는 메서드 체이닝을 통해서 검증할 수 있다.
        assertThat(article.getId())
                .isNotNull()
                .isEqualTo(1L)
                ;

        assertThat(article)
                .hasNoNullFieldsOrProperties()                          // 객체의 모든 필드가 null이 아닌지 확인
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("board.id", 10L)
                .hasFieldOrPropertyWithValue("subject", "updated subject")
                .hasFieldOrPropertyWithValue("content", "updated content")
                .hasFieldOrProperty("createdDate")
                ;

    }

    @Test
    @DisplayName("assertj BDD 스타일 - 게시글 수정")
    void updateArticleWithBDD() {
        // given
        var article = Article.builder()
                .id(1L)
                .board(board)
                .username("user")
                .subject("subject")
                .content("content")
                .createdAt(LocalDateTime.now())
                .build();

        // when
        String updateSubject = "updated subject";
        String updateContent = "updated content";
        article.update(updateSubject, updateContent);

        // then
        then(article)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("board.id", 10L)
                .hasFieldOrPropertyWithValue("subject", "updated subject")
                .hasFieldOrPropertyWithValue("content", "updated content")
                .hasFieldOrProperty("createdDate");
    }
}
