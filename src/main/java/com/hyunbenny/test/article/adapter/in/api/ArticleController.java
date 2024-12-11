package com.hyunbenny.test.article.adapter.in.api;

import com.hyunbenny.test.article.adapter.in.api.dto.ArticleDto;
import com.hyunbenny.test.article.application.port.in.CreateArticleUseCase;
import com.hyunbenny.test.article.application.port.in.DeleteArticleUseCase;
import com.hyunbenny.test.article.application.port.in.GetArticleUseCase;
import com.hyunbenny.test.article.application.port.in.ModifyArticleUseCase;
import com.hyunbenny.test.common.api.dto.CommandResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final GetArticleUseCase getArticleUseCase;
    private final CreateArticleUseCase createArticleUseCase;
    private final ModifyArticleUseCase modifyArticleUseCase;
    private final DeleteArticleUseCase deleteArticleUseCase;

    public ArticleController(GetArticleUseCase getArticleUseCase, CreateArticleUseCase createArticleUseCase,
                             ModifyArticleUseCase modifyArticleUseCase, DeleteArticleUseCase deleteArticleUseCase) {
        this.getArticleUseCase = getArticleUseCase;
        this.createArticleUseCase = createArticleUseCase;
        this.modifyArticleUseCase = modifyArticleUseCase;
        this.deleteArticleUseCase = deleteArticleUseCase;
    }

    @GetMapping("/{articleId}")
    ArticleDto.ArticleResponse getArticle(@PathVariable("articleId") Long articleId) {
        var article = getArticleUseCase.getArticleById(articleId);

        return ArticleDto.ArticleResponse.of(article);
    }

    @GetMapping(params = "boardId")
    List<ArticleDto.ArticleResponse> listArticlesByBoard(@RequestParam("boardId") Long boardId) {
        return getArticleUseCase.getArticlesByBoard(boardId).stream()
                .map(ArticleDto.ArticleResponse::of)
                .toList();
    }

    @PostMapping
    CommandResponse postArticle(@Valid @RequestBody ArticleDto.CreateArticleRequest request) {
        var createdArticle = createArticleUseCase.createArticle(request);
        return new CommandResponse(createdArticle.getId());
    }

    @PutMapping
    CommandResponse putArticle(@Valid @RequestBody ArticleDto.UpdateArticleRequest request) {
        var article = modifyArticleUseCase.modifyArticle(request);
        return new CommandResponse(article.getId());
    }

    @DeleteMapping("/{articleId}")
    void deleteArticled(@PathVariable("articleId") Long articleId) {
        deleteArticleUseCase.deleteArticle(articleId);
    }
}
