package com.hyunbenny.test;

import com.hyunbenny.test.article.adapter.in.api.dto.ArticleDto;
import com.hyunbenny.test.article.application.port.out.CommandArticlePort;
import com.hyunbenny.test.article.application.port.out.LoadArticlePort;
import com.hyunbenny.test.article.application.port.out.LoadBoardPort;
import com.hyunbenny.test.article.application.service.ArticleService;
import com.hyunbenny.test.article.domain.Board;
import com.hyunbenny.test.testFixtures.ArticleFixtures;
import com.hyunbenny.test.testFixtures.BoardFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ParameterizedAnnotationTest {

    private ArticleService sut;

    @Mock
    private LoadArticlePort loadArticlePort;

    @Mock
    private CommandArticlePort commandArticlePort;

    @Mock
    private LoadBoardPort loadBoardPort;

    private final Board board = BoardFixtures.board();

    @BeforeEach
    void setup() {
        sut = new ArticleService(loadArticlePort, commandArticlePort, loadBoardPort);
    }

    @ParameterizedTest
    @ValueSource(strings = {"english", "한글", "!@#$"})
    @DisplayName("영어, 한글, 특수문자로 제목을 생성해본다.")
    void subjectParameterizedTest(String subject) {
        var request = new ArticleDto.CreateArticleRequest(5L, subject, "content", "user");
        var createdArticle = ArticleFixtures.article();

        given(loadBoardPort.findBoardById(any())).willReturn(Optional.of(board));
        given(commandArticlePort.createArticle(any())).willReturn(createdArticle);

        var result = sut.createArticle(request);

        then(result).isEqualTo(createdArticle);
    }

    @ParameterizedTest
    @NullAndEmptySource // @ValueSource(strings = {null, ""})와 동일(@ValueSource에 null을 넣을 수는 없다)
    @DisplayName("null과 빈 값을 넘기는 경우 IllegalArgumentException을 발생시킨다.")
    void givenIsEmpty_thenThrowIllegalArgumentException(String subject) {
        var request = new ArticleDto.CreateArticleRequest(5L, subject, "content", "user");

        thenThrownBy(() -> sut.createArticle(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource(value = {
            "1, 3, 4",
            "132, 987, 1119",
            "19, 49, 68"
//            ", '', 13" // null, empty, integer
    })
    @DisplayName("csv파일을 이용하여 테스트의 변수를 변경하며 두 수의 합을 테스트를 해본다.")
    void usingCsvSourceTest(Integer a, Integer b, Integer sum) {
        then(sum).isEqualTo(a + b);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidParameters")
    @DisplayName("정상적이지 않은 파라미터인 경우, IllegalArgumentException을 발생시킨다.")
    void usingMethodSourceTest(String name, String subject, String content, String username) {
        var request = new ArticleDto.CreateArticleRequest(5L, subject, content, username);

        thenThrownBy(() -> sut.createArticle(request)).isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<Arguments> invalidParameters() {
        return Stream.of(
                Arguments.of("subject is null", null, "content", "user"),
                Arguments.of("subject is empty", "", "content", "user"),
                Arguments.of("content is null", "subject", null, "user"),
                Arguments.of("content is empty", "subject", "", "user"),
                Arguments.of("username is null", "subject", "content", null)
        );
    }
}
