package com.hyunbenny.test.article.adapter.out.persistence.respository;

import com.hyunbenny.test.article.adapter.out.persistence.entity.ArticleJpaEntity;
import com.hyunbenny.test.article.adapter.out.persistence.entity.BoardJpaEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

@DataJpaTest
class BasicArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private TestEntityManager entityManager;

    private BoardJpaEntity boardJpaEntity;

    @BeforeEach
    void setup() {
        boardJpaEntity = entityManager.persist(new BoardJpaEntity("test"));
        entityManager.persist(new ArticleJpaEntity(boardJpaEntity, "subject1", "content1", "user", LocalDateTime.now()));
        entityManager.persist(new ArticleJpaEntity(boardJpaEntity, "subject2", "content2", "user", LocalDateTime.now()));
    }

    @Test
    void listAllArticles() {
        var result = articleRepository.findByBoardId(boardJpaEntity.getId());
        BDDAssertions.then(result).hasSize(2);
    }

}