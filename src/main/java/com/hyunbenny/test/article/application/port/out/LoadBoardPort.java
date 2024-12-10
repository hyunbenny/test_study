package com.hyunbenny.test.article.application.port.out;

import com.hyunbenny.test.article.domain.Board;

import java.util.Optional;

public interface LoadBoardPort {
    Optional<Board> findBoardById(Long boardId);
}
