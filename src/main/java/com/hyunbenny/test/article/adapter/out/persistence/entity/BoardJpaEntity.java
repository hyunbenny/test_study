package com.hyunbenny.test.article.adapter.out.persistence.entity;

import com.hyunbenny.test.article.domain.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board")
@NoArgsConstructor
@Getter
public class BoardJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public BoardJpaEntity(String name) {
        this.name = name;
    }

    private BoardJpaEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Board toDomain() {
        return new Board(this.id, this.name);
    }

    public static BoardJpaEntity fromDomain(Board board) {
        return new BoardJpaEntity(board.getId(), board.getName());
    }
}
