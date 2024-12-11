package com.hyunbenny.test.article.adapter.out.persistence.respository;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest // @Transactional이 포함되어 있기 때문에 따로 @Transactional을 붙일 필요가 없다. 테스트가 끝나면 롤백된다.
@Sql("/data/ArticleRepositoryFixtureTest.sql")
class SQLArticleRepositoryTest {

    @Autowired
    private ArticleRepository repository;

    @Test
    void listAllArticles() {
        var result = repository.findByBoardId(5L);
        BDDAssertions.then(result).hasSize(2);
    }

    @Test
    @Sql("/data/ArticleRepositoryFixtureTest.listAllArticles2.sql")
    void listAllArticles2() {
        var result = repository.findByBoardId(5L);
        BDDAssertions.then(result).hasSize(3);
    }

}