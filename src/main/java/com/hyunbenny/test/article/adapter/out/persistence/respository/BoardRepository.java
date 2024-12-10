package com.hyunbenny.test.article.adapter.out.persistence.respository;

import com.hyunbenny.test.article.adapter.out.persistence.entity.BoardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardJpaEntity, Long> {
}
