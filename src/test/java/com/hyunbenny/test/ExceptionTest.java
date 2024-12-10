package com.hyunbenny.test;

import com.hyunbenny.test.article.adapter.in.api.dto.ArticleDto;
import com.hyunbenny.test.article.application.port.out.CommandArticlePort;
import com.hyunbenny.test.article.application.port.out.LoadArticlePort;
import com.hyunbenny.test.article.application.port.out.LoadBoardPort;
import com.hyunbenny.test.article.application.service.ArticleService;
import com.hyunbenny.test.article.domain.Board;
import com.hyunbenny.test.testFixtures.BoardFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;


@ExtendWith(MockitoExtension.class)
public class ExceptionTest {

    private ArticleService sut;

    @Mock
    private LoadArticlePort loadArticlePort;

    @Mock
    private CommandArticlePort commandArticlePort;

    @Mock(strictness = Mock.Strictness.LENIENT) // LENIENT: 불필요한 stub이 있더라도 무시하고 테스트를 진행하겠다.
    private LoadBoardPort  loadBoardPort;

    private final Board board = BoardFixtures.board();

    @BeforeEach
    void setup() {
        sut = new ArticleService(loadArticlePort, commandArticlePort, loadBoardPort);
    }

    @Test
    @DisplayName("subject가 정상적이지 않으면 IllegalArgumentException이 발생한다.")
    void givenSubject_when_subjectIsInvalid_thenThrowIllegalArgumentException() {
//        // given
        var request = new ArticleDto.CreateArticleRequest(5L, null, "content", "user");
//        BDDMockito.given(loadBoardPort.findBoardById(5L)).willReturn(Optional.of(board)); // 불필요한 stub이므로 주석처리

        thenThrownBy(() -> sut.createArticle(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not empty")
                .hasMessage("subject should not empty");
    }
}
