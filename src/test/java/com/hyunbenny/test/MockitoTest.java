package com.hyunbenny.test;

import com.hyunbenny.test.article.application.port.out.CommandArticlePort;
import com.hyunbenny.test.article.application.port.out.LoadArticlePort;
import com.hyunbenny.test.article.application.port.out.LoadBoardPort;
import com.hyunbenny.test.article.application.service.ArticleFixtures;
import com.hyunbenny.test.article.application.service.ArticleService;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MockitoTest {

    // 보통
    private ArticleService sut; // system under test: 테스트 대상이 되는 시스템

    @Mock
    private LoadArticlePort loadArticlePort;
    private CommandArticlePort commandArticlePort;
    private LoadBoardPort loadBoardPort;

    /**
     * Mock 객체를 생성하는 방법은 2가지가 있다.
     * 1. loadArticlePort와 같이 @Mock 어노테이션을 사용하는 방법.
            - @ExtendWith(MockitoExtension.class) 어노테이션을 같이 사용해야 Mockito가 Mock객체를 생성해준다.
     * 2. 아래의 commandArticlePort와 같이 Mockito.mock() 메서드를 사용하는 방법.
     */

    @BeforeEach
    void setup() {
//        loadArticlePort = mock(LoadArticlePort.class);
        commandArticlePort = mock(CommandArticlePort.class);
        loadBoardPort = mock(LoadBoardPort.class);

        sut = new ArticleService(loadArticlePort, commandArticlePort, loadBoardPort);
    }

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
    @DisplayName("BDD 스타일 - Board의 Article 목록 조회")
    void givenBoardId_whenGetArticlesByBoardId_thenReturnArticles() {
        var boardId = 5L;
        var article1 = ArticleFixtures.article(1L);
        var article2 = ArticleFixtures.article(2L);
        BDDMockito.given(loadArticlePort.findArticlesByBoardId(any())).willReturn(List.of(article1, article2));

        var result = sut.getArticlesByBoard(boardId);

        BDDAssertions.then(result)
                .hasSize(2)
                .extracting("board.id")
                .containsOnly(5L)
        ;

        // stub의 경우, 동작에 대한 검증이 아니라 결과값에 대한 검증을 수행한다.
        // -> 동작에 대한 검증(행위 검증)의 경우, 상태 검증보다 특정 메서드 호출과 같은 것들을 검증하기 때문에 구현에 의존적이게 된다.
        // (프로덕션 코드가 변경되면 테스트 코드가 변경될 확률이 높음)
        // 아래는 동작에 대한 검증의 예
        verify(loadArticlePort).findArticlesByBoardId(boardId); // findArticlesByBoardId()를 호출/실행했는지 검증

        // 단위테스트에서는 메인 코드의 수정이 테스트 코드에 영향을 주지 않도록 테스트 코드가 작성되어야 한다.
        // -> 내부 코드는 모르겠고 결과 값만 이렇게 나오면 된다.
    }

    // 위에서 `내부 코드는 모르겠고 결과 값만 이렇게 나오면 된다.`고 했는데 삭제와 같은 경우, 리턴 값이 없기 때문에 내부 메서드가 실행되었는지 동작 검증을 해야 한다.
    @Test
    @DisplayName("Article 삭제")
    void deleteArticle() {
        BDDMockito.willDoNothing().given(commandArticlePort).deleteArticle(any());

        sut.deleteArticle(1L);

        verify(commandArticlePort).deleteArticle(1L); //
    }

}
