package com.hyunbenny.test;

import com.hyunbenny.test.article.domain.Article;
import com.hyunbenny.test.article.domain.Board;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class JunitTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all: 테스트 클래스 실행 전 한번만 실행된다.\n");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("\nAfter all: 테스트 클래스 실행 후 한번만 실행된다.");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("Before each: 각 테스트 메서드 실행 전 실행된다.");
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each: 각 테스트 매서느 실행 후 실행된다.");
    }

    @Test
    @DisplayName("테스트 의 순서는 @BeforeAll -> @BeforeEach -> @Test -> @AfterEach -> @AfterAll 순으로 동작한다.")
    void test1() {
        System.out.println("단, 개별 단위테스트의 순서는 보장되지 않는다.");
    }

    @Test
    @DisplayName("test2")
    void test2() {
        System.out.println("테스트 성공");
    }

    @Test
    @Disabled
    @DisplayName("@Disabled는 테스트는 만들어놓고 실행하지 않는 경우 사용한다. 예)운영환경에서는 실행되면 안되는 테스트가 있는 경우.. 사유를 적어놓는다.")
    void disabledTest() {
        System.out.println("@Disabled는 실행되지 않는다.");
    }

    @Test
    @DisplayName("객체 생성 테스트")
    void articleConstructorTest() {
        var board = Board.builder().id(5L).name("board").build();

        var article = Article.builder()
                .id(1L)
                .username("user")
                .subject("subject")
                .content("content")
                .board(board)
                .createdDate(LocalDateTime.now())
                .build();


        // assert
        assertEquals(1L, article.getId());
        assertTrue(article.getBoard().equals(board));
        assertEquals("subject", article.getSubject());
        assertEquals("content", article.getContent());
        assertNotEquals("contentttt", article.getContent());
        assertNotNull(article.getCreatedDate());

    }

    @Test
    @DisplayName("실패 테스트 예시")
    void failTest() {
        assertEquals(4, 3 + 1, "테스트 실패시 출력되는 메시지");
//        assertEquals(4, 2 + 1, "테스트 실패시 출력되는 메시지");
    }

}
