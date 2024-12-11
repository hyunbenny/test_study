package com.hyunbenny.test;

import com.hyunbenny.test.article.adapter.in.api.dto.ArticleDto;
import com.hyunbenny.test.article.application.port.out.CommandArticlePort;
import com.hyunbenny.test.article.application.port.out.LoadArticlePort;
import com.hyunbenny.test.article.application.port.out.LoadBoardPort;
import com.hyunbenny.test.article.application.service.ArticleService;
import com.hyunbenny.test.article.domain.Article;
import com.hyunbenny.test.common.exception.ResourceNotFoundException;
import com.hyunbenny.test.testFixtures.ArticleFixtures;
import com.hyunbenny.test.testFixtures.BoardFixtures;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class NestedTest {

    private ArticleService sut;

    @Mock
    private LoadArticlePort loadArticlePort;
    @Mock
    private CommandArticlePort commandArticlePort;
    @Mock
    private LoadBoardPort loadBoardPort;

    @BeforeEach
    void setup() {
        sut = new ArticleService(loadArticlePort, commandArticlePort, loadBoardPort);
    }

    // 보통 테스트를 그루핑하여 구분하기 위해서 사용한다. 예) Article 조회 클래스를 만들고 하위에는 조회 시 성공/실패 케이스에 대해서만 테스트를 작성하는 등.
    @Nested
    @DisplayName("Article 조회")
    class GetArticle {

        @Test
        @DisplayName("articeId로 조회 시 Article을 반환한다.")
        void givenArticleId_whenGetArticleById_thenReturnArticle() {
            var article = ArticleFixtures.article();
            Mockito.when(loadArticlePort.findArticleById(any())).thenReturn(Optional.of(article));

            var result = sut.getArticleById(1L);

            BDDAssertions.then(result)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("id", article.getId())
                    .hasFieldOrPropertyWithValue("board.id", article.getBoard().getId())
                    .hasFieldOrPropertyWithValue("subject", article.getSubject())
                    .hasFieldOrPropertyWithValue("content", article.getContent())
                    .hasFieldOrPropertyWithValue("createdAt", article.getCreatedAt())
            ;
        }

        @Test
        @DisplayName("Article이 존재하지 않으면 ResourceNotFoundException을 던진다.")
        void givenArticleId_whenGetArticleById_thenThrowResourceNotFoundException() {
            Mockito.when(loadArticlePort.findArticleById(any())).thenReturn(Optional.empty());

            BDDAssertions.thenThrownBy(() -> sut.getArticleById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("게시물이 없습니다.")
                    .hasMessage(String.format("id: %s 게시물이 없습니다.", 1))
            ;
        }

    }


    @Nested
    @DisplayName("Article 생성")
    class CreateArticle {
        @Test
        @DisplayName("객체 생성 성공")
        void articleConstructorTest() {
            var board = BoardFixtures.board();
            var request = new ArticleDto.CreateArticleRequest(board.getId(), "subject1", "content1", "user1");
            Article article = ArticleFixtures.article();
            BDDMockito.given(loadBoardPort.findBoardById(any())).willReturn(Optional.of(board));
            BDDMockito.given(commandArticlePort.createArticle(any(Article.class))).willReturn(article);

            var createdArticle = sut.createArticle(request);

            BDDAssertions.then(createdArticle)
                    .hasNoNullFieldsOrProperties()
                    .hasFieldOrPropertyWithValue("board.id", 5L)
                    .hasFieldOrPropertyWithValue("subject", "subject1")
                    .hasFieldOrPropertyWithValue("content", "content1")
                    .hasFieldOrPropertyWithValue("username", "user1")
                    .hasFieldOrProperty("createdAt");
        }

        @Test
        @DisplayName("객체 생성 실패 - subject가 null이면 IllegalArgumentException이 발생한다.")
        void givenNullSubject_whenCreateArticle_thenThrowIllegalArgumentException() {
            var request = new ArticleDto.CreateArticleRequest(5L, null, "content", "user");

            BDDAssertions.thenThrownBy(() -> sut.createArticle(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("not empty")
                    .hasMessage("subject should not empty");
        }

        @Test
        @DisplayName("객체 생성 실패 - subject가 empty이면 IllegalArgumentException이 발생한다.")
        void givenEmptySubject_whenCreateArticle_thenThrowIllegalArgumentException() {
            var request = new ArticleDto.CreateArticleRequest(5L, "", "content", "user");

            BDDAssertions.thenThrownBy(() -> sut.createArticle(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("not empty")
                    .hasMessage("subject should not empty");
        }
    }


}

