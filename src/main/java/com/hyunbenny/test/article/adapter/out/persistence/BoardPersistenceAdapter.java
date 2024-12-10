package com.hyunbenny.test.article.adapter.out.persistence;

import com.hyunbenny.test.article.adapter.out.persistence.entity.BoardJpaEntity;
import com.hyunbenny.test.article.adapter.out.persistence.respository.BoardRepository;
import com.hyunbenny.test.article.application.port.out.LoadBoardPort;
import com.hyunbenny.test.article.domain.Board;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BoardPersistenceAdapter implements LoadBoardPort {
    private final BoardRepository boardRepository;

    public BoardPersistenceAdapter(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Optional<Board> findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .map(BoardJpaEntity::toDomain);
    }
}
