package com.hyunbenny.test.article.adapter.in.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunbenny.test.article.adapter.in.api.dto.ArticleDto;
import com.hyunbenny.test.article.application.port.in.CreateArticleUseCase;
import com.hyunbenny.test.article.application.port.in.DeleteArticleUseCase;
import com.hyunbenny.test.article.application.port.in.GetArticleUseCase;
import com.hyunbenny.test.article.application.port.in.ModifyArticleUseCase;
import com.hyunbenny.test.common.exception.AccessDeniedException;
import com.hyunbenny.test.common.exception.ResourceNotFoundException;
import com.hyunbenny.test.testFixtures.ArticleFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleController.class)
class MockBeanArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean // @Mock + @Bean
    private GetArticleUseCase getArticleUseCase;
    @MockBean
    private CreateArticleUseCase createArticleUseCase;
    @MockBean
    private ModifyArticleUseCase modifyArticleUseCase;
    @MockBean
    private DeleteArticleUseCase deleteArticleUseCase;

    @Nested
    @DisplayName("[GET] /articles/{articleId}")
    class GetArticle {
        @Test
        @DisplayName("Article이 있으면, 상태코드 200을 반환한다.")
        void returnResponse() throws Exception {
            var article = ArticleFixtures.article();
            given(getArticleUseCase.getArticleById(any()))
                    .willReturn(article);

            Long articleId = 1L;
            mockMvc.perform(get("/articles/{articleId}", articleId))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("articleId 에 해당하는 Article이 없으면 상태코드 400을 반환한다.")
        void notFound() throws Exception {
            given(getArticleUseCase.getArticleById(any()))
                    .willThrow(new ResourceNotFoundException("article not exists"));

            Long articleId = 1L;
            mockMvc.perform(get("/articles/{articleId}", articleId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("[POST] /articles")
    class PostArticle {
        @Test
        @DisplayName("생성된 articleId를 반환한다.")
        void create_returnArticleId() throws Exception {
            var createdArticle = ArticleFixtures.article();
            given(createArticleUseCase.createArticle(any()))
                    .willReturn(createdArticle);

            var body = objectMapper.writeValueAsString(Map.of("boardId", 5L, "subject", "subject", "content", "content", "username", "user"));
            mockMvc.perform(post("/articles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
            ;
        }

        @ParameterizedTest(name = "{0}")
        @DisplayName("파라미터가 비정상인 경우 상태코드 400을 반환한다.")
        @CsvSource(value =
                {
                        "subject가 null인 경우,,content,user",
                        "conten가 null인 경우,subject,,user",
                        "username이 null인 경우,subject,content,",
                        "username이 empty인 경우,subject,content,''"
                }
        )
        void invalidParam_BadRequest(String desc, String subject, String content, String username) throws Exception {
            var body = objectMapper.writeValueAsString(new ArticleDto.CreateArticleRequest(5L, subject, content, username));
            mockMvc.perform(post("/articles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
            ;
        }
    }

    @Nested
    @DisplayName("[PUT] /articles")
    class PutArticle {
        @Test
        @DisplayName("변경된 Article의 articleId 반환한다.")
        void returnArticleId() throws Exception {
            var modifiedArticle = ArticleFixtures.article();
            given(modifyArticleUseCase.modifyArticle(any()))
                    .willReturn(modifiedArticle);

            var body = objectMapper.writeValueAsString(Map.of("id", 1L, "board", Map.of("id", 5L, "name", "board"), "subject", "new subject", "content", "new content", "username", "user"));
            mockMvc
                    .perform(
                            put("/articles")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpect(
                            status().isOk()
                    );
        }

        @ParameterizedTest(name = "{0}")
        @DisplayName("파라미터가 비정상인 경우 상태코드 400을 반환한다.")
        @CsvSource(
                value = {
                        "subject가 null인 경우,,content,user",
                        "content가 null인 경우,subject,,user"
                }
        )
        void invalidParam_BadRequest(String desc, String subject, String content, String username) throws Exception {
            var body = objectMapper.writeValueAsString(new ArticleDto.CreateArticleRequest(5L, subject, content, username));
            mockMvc
                    .perform(
                            put("/articles")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpect(
                            status().isBadRequest()
                    );
        }

        @Test
        @DisplayName("작성자가 아닌 다른 사용자가 수정을 시도하면 상태코드 403을 반환한다.")
        void otherUser_Forbidden() throws Exception {
            given(modifyArticleUseCase.modifyArticle(any()))
                    .willThrow(new AccessDeniedException("작성자가 아닌 사용자는 수정이 불가능합니다."));

            var body = objectMapper.writeValueAsString(Map.of("id", 1L, "board", Map.of("id", 5L, "name", "board"), "subject", "new subject", "content", "new content", "username", "otheruser"));
            mockMvc
                    .perform(
                            put("/articles")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpect(
                            status().isForbidden()
                    );
        }
    }

    @Test
    @DisplayName("[DELETE] /articles/{articleId}")
    void deleteArticle() throws Exception {
        willDoNothing().given(deleteArticleUseCase).deleteArticle(any());

        mockMvc.perform(delete("/articles/{articleId}", 1L))
                .andDo(print())
                .andExpect(
                        status().isOk()
                );

        verify(deleteArticleUseCase).deleteArticle(1L);
    }

}